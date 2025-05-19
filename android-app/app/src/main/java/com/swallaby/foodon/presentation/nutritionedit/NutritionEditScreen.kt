package com.swallaby.foodon.presentation.nutritionedit

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swallaby.foodon.R
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.component.CommonBackTopBar
import com.swallaby.foodon.core.ui.component.CommonWideButton
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.bottomBorder
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import com.swallaby.foodon.core.util.NumberFormatPattern
import com.swallaby.foodon.domain.food.model.MealItem
import com.swallaby.foodon.domain.food.model.NutrientConverter
import com.swallaby.foodon.domain.food.model.NutrientType
import com.swallaby.foodon.presentation.foodedit.viewmodel.FoodEditEvent
import com.swallaby.foodon.presentation.foodedit.viewmodel.FoodEditViewModel
import com.swallaby.foodon.presentation.nutritionedit.component.NutrientField
import kotlin.math.min

@Composable
fun NutritionEditScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: FoodEditViewModel,
    foodId: Long = 0,
    onFoodUpdateClick: (mealItem: MealItem) -> Unit = {},
    onSuccessCustomFood: (mealItem: MealItem) -> Unit = {},
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val mealInfo = (uiState.foodEditState as ResultState.Success).data
    val food = mealInfo.mealItems.find { item ->
        item.foodId == foodId
    }!!

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is FoodEditEvent.SuccessCustomFood -> {
                    onSuccessCustomFood(event.mealItem)
                }

                is FoodEditEvent.FailedCustomFood -> {
                    Toast.makeText(context, event.messageRes, Toast.LENGTH_SHORT).show()
                }

                else -> {
                }
            }
        }
    }

    var nutritions by remember(uiState) {
        mutableStateOf(NutrientConverter.convertToHierarchy(food.nutrientInfo))
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is FoodEditEvent.SuccessUpdateFood -> {
                    onBackClick()
                }

                else -> {}
            }
        }
    }

    val scrollState = rememberScrollState()


    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .imePadding()
        ) {
            CommonBackTopBar(
                title = stringResource(R.string.top_bar_nutrient_input), onBackClick = onBackClick
            )

            Column(
                modifier = modifier
                    .weight(1f)
                    .padding(24.dp)
                    .verticalScroll(scrollState),
            ) {
                Column(
                    modifier = modifier
                        .height(70.dp)
                        .fillMaxWidth()
                        .bottomBorder()
                ) {
                    Text(food.foodName, style = NotoTypography.NotoBold18.copy(color = G900))
                    Spacer(modifier = modifier.height(4.dp))
                    Text("200g", style = SpoqaTypography.SpoqaMedium16.copy(color = G700))
                }
                Box(
                    modifier = modifier
                        .height(62.dp)
                        .padding(top = 24.dp)
                ) {
                    Text(
                        stringResource(R.string.nutritional_ingredients),
                        style = NotoTypography.NotoBold18.copy(color = G900)
                    )
                }

                // 영양소 항목들을 렌더링하고 값 업데이트 처리
                nutritions.forEachIndexed { index, item ->
                    NutrientField(
                        modifier = modifier,
                        value = item.value.toString(),
                        formatPattern = if (item.nutrientType == NutrientType.KCAL) NumberFormatPattern.INT_THOUSAND_COMMA else NumberFormatPattern.DOUBLE_THOUSAND_COMMA,
                        onValueChange = { newValue ->
                            val filtered =
                                newValue.filter { it.isDigit() || it == '.' }.toBigDecimalOrNull()
                                    ?.toDouble() ?: 0.0
                            val updatedValue = min(filtered, 999999.99)

                            // 업데이트된 아이템 생성
                            val updatedItem = item.copy(value = updatedValue)
                            // 리스트에서 해당 아이템 교체
                            val updatedNutritions = nutritions.toMutableList()
                            updatedNutritions[index] = updatedItem
                            nutritions = updatedNutritions
                        },
                        nutrient = item.name,
                        unit = item.unit,
                        isLastField = index == nutritions.size - 1 && item.childItems.isEmpty()
                    )

                    // 자식 항목들 처리
                    item.childItems.forEachIndexed { childIndex, childItem ->
                        NutrientField(
                            modifier = modifier,
                            value = childItem.value.toString(),
                            onValueChange = { newValue ->
                                val filtered = newValue.filter { it.isDigit() || it == '.' }
                                    .toBigDecimalOrNull()?.toDouble() ?: 0.0
                                val updatedValue = min(filtered, 999999.99)

                                // 업데이트된 자식 아이템 생성
                                val updatedChildItem = childItem.copy(value = updatedValue)
                                // 부모 아이템의 자식 목록 업데이트
                                val updatedChildItems = item.childItems.toMutableList()
                                updatedChildItems[childIndex] = updatedChildItem

                                // 부모 아이템 업데이트
                                val updatedItem = item.copy(childItems = updatedChildItems)

                                // 전체 리스트 업데이트
                                val updatedNutritions = nutritions.toMutableList()
                                updatedNutritions[index] = updatedItem
                                nutritions = updatedNutritions
                            },
                            nutrient = childItem.name,
                            unit = childItem.unit,
                            isChildField = true,
                            isLastField = index == nutritions.size - 1 && childIndex == item.childItems.size - 1
                        )
                    }
                }
            }

            CommonWideButton(
                text = stringResource(R.string.btn_update),
                modifier = modifier.padding(horizontal = 24.dp),
                onClick = {
                    val updatedNutrientInfo = NutrientConverter.updateNutrientInfo(
                        nutritions, food.nutrientInfo
                    )
                    val updateMealItem = MealItem(
                        foodId = food.foodId,
                        foodName = food.foodName,
                        quantity = food.quantity,
                        unit = food.unit,
                        nutrientInfo = updatedNutrientInfo
                    )
                    onFoodUpdateClick(updateMealItem)
                },
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NutritionEditScreenPreview() {
    NutritionEditScreen(onBackClick = {}, viewModel = hiltViewModel())
}

