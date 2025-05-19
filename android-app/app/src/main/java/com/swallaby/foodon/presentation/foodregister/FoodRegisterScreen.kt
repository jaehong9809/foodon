package com.swallaby.foodon.presentation.foodregister

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.component.CommonBackTopBar
import com.swallaby.foodon.core.ui.component.CommonWideButton
import com.swallaby.foodon.core.ui.component.OutLineTextField
import com.swallaby.foodon.core.ui.theme.FoodonTheme
import com.swallaby.foodon.core.ui.theme.G500
import com.swallaby.foodon.core.ui.theme.G750
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.bottomBorder
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import com.swallaby.foodon.core.util.IntegerVisualTransformation
import com.swallaby.foodon.core.util.NumberFormatPattern
import com.swallaby.foodon.domain.food.model.FoodInfo
import com.swallaby.foodon.domain.food.model.NutrientConverter
import com.swallaby.foodon.domain.food.model.NutrientInfo
import com.swallaby.foodon.domain.food.model.NutrientType
import com.swallaby.foodon.domain.food.model.UnitType
import com.swallaby.foodon.presentation.foodedit.component.UnitTypeChip
import com.swallaby.foodon.presentation.foodregister.viewmodel.FoodRegisterEvent
import com.swallaby.foodon.presentation.foodregister.viewmodel.FoodRegisterViewModel
import com.swallaby.foodon.presentation.mealdetail.component.DropButton
import com.swallaby.foodon.presentation.mealdetail.dismissModalBottomSheet
import com.swallaby.foodon.presentation.nutritionedit.component.NutrientField
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodRegisterScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: FoodRegisterViewModel = hiltViewModel(),
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is FoodRegisterEvent.NavigateToSearch -> {
                    onBackClick()
                }

                is FoodRegisterEvent.ShowErrorMessage -> {
                    Toast.makeText(context, event.errorMessageRes, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    var nutritions by remember {
        mutableStateOf(NutrientConverter.convertToHierarchy(NutrientInfo()))
    }

    var foodName by remember {
        mutableStateOf("")
    }

    var servingSize by remember {
        mutableStateOf<Int?>(null)
    }

    var unit by remember {
        mutableStateOf(UnitType.GRAM)
    }

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val isEnabled by remember {
        derivedStateOf {
            foodName.isNotEmpty() && servingSize != null && servingSize != 0
        }
    }



    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .imePadding()
        ) {
            CommonBackTopBar(
                title = stringResource(R.string.top_bar_input), onBackClick = onBackClick
            )
            Column(
                modifier = modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp)
            ) {
                Box(
                    modifier.bottomBorder()
                ) {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.food_name),
                            style = NotoTypography.NotoBold18.copy(color = G900)
                        )
                        Spacer(modifier = modifier.height(8.dp))
                        OutLineTextField(
                            modifier = modifier
                                .height(48.dp)
                                .fillMaxWidth(),
                            value = foodName,
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next
                            ),
                            onValueChange = { it -> foodName = it },
                            textStyle = SpoqaTypography.SpoqaMedium16.copy(
                                color = G900
                            ),
                            placeholder = {
                                Text(
                                    stringResource(R.string.food_name_placeholder),
                                    style = SpoqaTypography.SpoqaMedium16.copy(color = G500)
                                )
                            })
                    }
                }


                Box(
                    modifier.bottomBorder()
                ) {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.food_quantity),
                            style = NotoTypography.NotoBold18.copy(color = G900)
                        )
                        Spacer(modifier = modifier.height(12.dp))
                        Row(modifier = modifier.fillMaxWidth()) {
                            OutLineTextField(
                                modifier = modifier
                                    .height(48.dp)
                                    .weight(1f),
                                value = if (servingSize == null) "" else servingSize.toString(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                                ),
                                visualTransformation = IntegerVisualTransformation(maxValue = 999999),
                                onValueChange = { newValue ->
                                    // 숫자만 필터링
                                    val filterValue =
                                        newValue.filter { it.isDigit() }.toIntOrNull() ?: 0
                                    servingSize = min(filterValue, 999999)
                                },
                                textStyle = SpoqaTypography.SpoqaMedium16.copy(
                                    color = G900
                                ),
                                placeholder = {
                                    Text(
                                        "0",
                                        style = SpoqaTypography.SpoqaMedium16.copy(color = G500)
                                    )
                                })
                            Spacer(modifier = modifier.width(8.dp))
                            DropButton(
                                modifier = modifier
                                    .height(48.dp)
                                    .weight(1f),
                                onClick = { showBottomSheet = true },
                                text = unit.value,
                                contentModifier = modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                                contentAlignment = Arrangement.SpaceBetween,
                                suffixIcon = {
                                    Image(
                                        painter = painterResource(R.drawable.icon_down_chevron),
                                        contentDescription = "down_chevron",
                                        colorFilter = ColorFilter.tint(color = G750)
                                    )
                                },
                                textStyle = SpoqaTypography.SpoqaNormal16.copy(color = G900)
                            )
                        }
                    }
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
                text = stringResource(R.string.btn_complete),
                modifier = modifier.padding(horizontal = 24.dp),
                isEnabled = isEnabled,
                onClick = {
                    val updatedNutrientInfo = NutrientConverter.updateNutrientInfo(
                        nutritions, NutrientInfo()
                    )
                    val foodInfo = FoodInfo(
                        foodName = foodName,
                        unit = unit,
                        servingSize = servingSize ?: 0,
                        nutrients = updatedNutrientInfo
                    )
                    viewModel.registerFood(foodInfo)
                },
            )

        }

        if (showBottomSheet) {
            ModalBottomSheet(dragHandle = null, sheetState = sheetState, onDismissRequest = {
                showBottomSheet = false
            }) {

                var selectedUnitType by remember {
                    mutableStateOf(unit)
                }

                Column(
                    modifier = modifier
                        .wrapContentHeight()
                        .background(Color.White)
                ) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = {
                            dismissModalBottomSheet(
                                scope = scope,
                                sheetState = sheetState,
                                callback = {
                                    showBottomSheet = false
                                },
                            )
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.icon_close),
                                contentDescription = "close"
                            )
                        }
                    }

                    UnitTypeBottomSheet(
                        initUnitType = unit,
                        onUnitSelected = {
                            selectedUnitType = it
                        },
                    )

                    CommonWideButton(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = "확인",
                        onClick = {
                            unit = selectedUnitType
                            dismissModalBottomSheet(
                                scope = scope,
                                sheetState = sheetState,
                                callback = {
                                    showBottomSheet = false
                                },
                            )
                        },
                    )

                }

            }
        }

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UnitTypeBottomSheet(
    modifier: Modifier = Modifier,
    initUnitType: UnitType,
    onUnitSelected: (UnitType) -> Unit,
) {
    var selectedUnitType by remember {
        mutableStateOf(initUnitType)
    }

    val categories = mapOf(
        "무게/열량" to listOf(UnitType.GRAM, UnitType.MILLIGRAM, UnitType.KILOGRAM, UnitType.KCAL),
        "부피" to listOf(UnitType.LITER, UnitType.ML, UnitType.CUP),
        "개수/수량" to listOf(
            UnitType.PIECE,
            UnitType.WHOLE,
            UnitType.EGG,
            UnitType.SLICE,
            UnitType.SHEET,
            UnitType.STRING,
            UnitType.SERVING
        ),
        "용기/포장" to listOf(
            UnitType.BOWL, UnitType.BOTTLE, UnitType.CAN, UnitType.BAG, UnitType.PLATE
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        // 카테고리별 표시
        categories.forEach { (category, unitTypes) ->
            Column(modifier = modifier.padding(vertical = 8.dp)) {
                Text(
                    text = category,
                    style = NotoTypography.NotoMedium16,
                )
                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    unitTypes.forEach { unitType ->
                        UnitTypeChip(
                            unit = unitType.value,
                            isSelected = unitType == selectedUnitType,
                            onClick = {
                                selectedUnitType = unitType
                                onUnitSelected(unitType)
                            })
                    }
                }
            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun FoodRegisterScreenPreview() {
    FoodonTheme {
        FoodRegisterScreen(onBackClick = {})
    }
}