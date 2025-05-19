package com.swallaby.foodon.presentation.foodedit

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swallaby.foodon.R
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.component.CommonBackTopBar
import com.swallaby.foodon.core.ui.component.CommonWideButton
import com.swallaby.foodon.core.ui.component.UpdateFoodButton
import com.swallaby.foodon.core.ui.theme.BG100
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.core.ui.theme.Border02
import com.swallaby.foodon.core.ui.theme.FoodonTheme
import com.swallaby.foodon.core.ui.theme.G500
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.G800
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.core.ui.theme.bottomBorder
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import com.swallaby.foodon.core.util.StringUtil
import com.swallaby.foodon.domain.food.model.FoodSimilar
import com.swallaby.foodon.domain.food.model.FoodType
import com.swallaby.foodon.domain.food.model.NutrientConverter
import com.swallaby.foodon.domain.food.model.NutrientItem
import com.swallaby.foodon.domain.food.model.NutrientType
import com.swallaby.foodon.presentation.foodedit.component.FoodAmountComponent
import com.swallaby.foodon.presentation.foodedit.component.FoodChip
import com.swallaby.foodon.presentation.foodedit.component.FoodThumbnailList
import com.swallaby.foodon.presentation.foodedit.component.SearchChip
import com.swallaby.foodon.presentation.foodedit.viewmodel.FoodEditViewModel
import com.swallaby.foodon.presentation.foodregister.UnitTypeBottomSheet
import com.swallaby.foodon.presentation.mealdetail.dismissModalBottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodEditScreen(
    modifier: Modifier = Modifier,
    mealId: Long = 0,
    viewModel: FoodEditViewModel,
    onBackClick: () -> Unit,
    onFoodDeleteClick: (foodId: Long) -> Unit = {},
    onFoodUpdateClick: () -> Unit = {},
    onNutritionEditClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val mealInfo = (uiState.foodEditState as ResultState.Success).data


    val enabledUpdate = remember(mealId) {
        mealId == 0L
    }


    val food = mealInfo.mealItems.singleOrNull { item ->
        item.foodId == uiState.selectedFoodId
    } ?: mealInfo.mealItems.first()


    // food.nutrientInfo 대신 uiState 자체를 의존성으로 설정
    var nutrientInfo by remember(uiState) {
        Log.d("Screen", "FoodEditScreen Update viewModel ${uiState}")
        mutableStateOf(NutrientConverter.convertToHierarchy(food.nutrientInfo))
    }


    Log.d("Screen", "FoodEditScreen ViewModel identity: ${System.identityHashCode(viewModel)}")
    Log.d("Screen", "FoodEditScreen Food.nutrientInfo: ${food.nutrientInfo}")
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()
    Scaffold { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            CommonBackTopBar(
                title = stringResource(R.string.top_bar_food_info_update), onBackClick = onBackClick
            )
            Column(
                modifier = modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                FoodThumbnailList(
                    foods = mealInfo.mealItems,
                    imageUri = mealInfo.imageUri,
                    selectedFoodId = uiState.selectedFoodId,
                    selectFood = viewModel::selectFood
                )
                if (enabledUpdate) HorizontalDivider(
                    modifier = modifier.padding(horizontal = 24.dp),
                    thickness = 1.dp,
                    color = Border02
                )
                if (enabledUpdate) FoodSearch(
                    foodName = food.foodName,
                    onSearchClick = onSearchClick,
                    selectedFoodId = uiState.selectedFoodId,
                    foodSimilarState = uiState.foodSimilarState,
                    onClick = { foodId -> viewModel.fetchFood(foodId, FoodType.PUBLIC) })
                Spacer(
                    modifier = modifier
                        .height(8.dp)
                        .fillMaxWidth()
                        .background(color = Bkg04)
                )
                FoodAmountComponent(
                    food = food,
                    enabledUpdate = enabledUpdate,
                    onUpdateQuantity = viewModel::updateQuantity,
                    onClickUnitType = {
                        showBottomSheet = true
                    })
                NutritionComponent(
                    modifier = modifier,
                    nutrientInfo = nutrientInfo,
                    onNutritionEditClick = onNutritionEditClick,
                    enabledUpdate
                )
            }
            if (enabledUpdate) UpdateFoodButton(
                modifier = modifier.padding(
                    horizontal = 24.dp
                ),
                onDeleteClick = { onFoodDeleteClick(food.foodId) },
                onUpdateClick = onFoodUpdateClick,
            )
        }

        if (showBottomSheet) {
            ModalBottomSheet(dragHandle = null, sheetState = sheetState, onDismissRequest = {
                showBottomSheet = false
            }) {

                var selectedUnitType by remember {
                    mutableStateOf(food.unit)
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
                        initUnitType = food.unit,
                        onUnitSelected = {
                            selectedUnitType = it
                        },
                    )

                    CommonWideButton(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = "확인",
                        onClick = {
                            viewModel.updateUnitType(
                                food.foodId, selectedUnitType
                            )
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

@Composable
fun NutritionComponent(
    modifier: Modifier = Modifier,
    nutrientInfo: List<NutrientItem>,
    onNutritionEditClick: () -> Unit = {},
    enabledUpdate: Boolean = true,
) {
    Column(modifier.padding(horizontal = 24.dp)) {
        Text(
            stringResource(R.string.nutritional_ingredients),
            style = NotoTypography.NotoBold18.copy(color = G900)
        )
        repeat(nutrientInfo.size) { index ->
            val childItems = nutrientInfo[index].childItems

            when (nutrientInfo[index].nutrientType) {
                NutrientType.KCAL -> {
                    ParentNutritionInfo(
                        nutritionName = nutrientInfo[index].name,
                        amount = stringResource(
                            R.string.format_kcal,
                            StringUtil.formatKcal(nutrientInfo[index].value.toInt())
                        ),
                        amountColor = WB500,
                    )
                }

                NutrientType.CHOLESTEROL, NutrientType.SODIUM, NutrientType.POTASSIUM -> {
                    ParentNutritionInfo(
                        nutritionName = nutrientInfo[index].name,
                        amount = StringUtil.formatNutrition(
                            nutrientInfo[index].value, defaultUnit = R.string.format_nutrition_mg
                        ),
                        hasChild = childItems.isNotEmpty()
                    )
                }

                else -> {
                    ParentNutritionInfo(
                        nutritionName = nutrientInfo[index].name,
                        amount = StringUtil.formatNutrition(nutrientInfo[index].value),
                        hasChild = childItems.isNotEmpty()
                    )
                }
            }

            repeat(childItems.size) { childIndex ->
                ChildNutritionInfo(
                    nutritionName = childItems[childIndex].name,
                    amount = StringUtil.formatNutrition(childItems[childIndex].value),
                )

            }
        }

        if (enabledUpdate) Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            TextButton(
                onClick = onNutritionEditClick,
                modifier = modifier
                    .height(48.dp)
                    .background(color = BG100, shape = RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                interactionSource = remember { MutableInteractionSource() },
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Text(
                    stringResource(R.string.top_bar_nutrient_input),
                    style = NotoTypography.NotoMedium16.copy(color = G800)
                )
            }
        }
        Spacer(modifier = modifier.height(24.dp))

    }

}

@Composable
private fun BaseNutritionInfo(
    modifier: Modifier = Modifier,
    nutrition: @Composable () -> Unit,
    amount: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        nutrition()
        amount()
    }
}


@Composable
private fun ParentNutritionInfo(
    modifier: Modifier = Modifier,
    nutritionName: String,
    amount: String,
    amountColor: Color = G900,
    hasChild: Boolean = false,
) {
    BaseNutritionInfo(modifier = if (hasChild) modifier.bottomBorder() else modifier, nutrition = {
        Text(nutritionName, style = NotoTypography.NotoMedium16.copy(color = G800))
    }, amount = {
        Text(amount, style = SpoqaTypography.SpoqaBold16.copy(color = amountColor))
    })
}

@Composable
private fun ChildNutritionInfo(
    modifier: Modifier = Modifier,
    nutritionName: String,
    amount: String,
) {
    BaseNutritionInfo(modifier = modifier, nutrition = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.icon_inner), contentDescription = "inner",
            )
            Spacer(modifier = modifier.width(2.dp))
            Text(nutritionName, style = NotoTypography.NotoMedium16.copy(color = G700))
        }
    }, amount = {
        Text(amount, style = SpoqaTypography.SpoqaBold16.copy(color = G500))
    })
}

@Composable
fun FoodSearch(
    modifier: Modifier = Modifier,
    foodName: String,
    foodSimilarState: ResultState<List<FoodSimilar>>,
    selectedFoodId: Long,
    onSearchClick: () -> Unit = {},
    onClick: (foodId: Long) -> Unit = {},
) {
    // LazyListState 사용
    val lazyRowState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.padding(top = 16.dp, bottom = 24.dp)) {
        Row(
            modifier = modifier.padding(horizontal = 24.dp)
        ) {
            Text(
                foodName, style = NotoTypography.NotoBold14.copy(color = G900)
            )
            Text("가 아닌가요?", style = NotoTypography.NotoBold14.copy(color = G500))
        }
        Spacer(modifier = modifier.height(8.dp))

        LazyRow(
            state = lazyRowState,
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // 검색 칩 추가
            item {
                SearchChip(modifier, onClick = onSearchClick)
            }

            // 음식 칩들 추가
            when (foodSimilarState) {
                is ResultState.Success -> {
                    val foodSimilars = foodSimilarState.data

                    // 각 음식에 대한 인덱스 맵 생성
                    val foodIndexMap = foodSimilars.mapIndexed { index, food ->
                        food.foodId to (index + 1) // SearchChip이 인덱스 0이므로 +1
                    }.toMap()

                    items(foodSimilars.size) { index ->
                        FoodChip(
                            modifier = modifier, food = foodSimilars[index], onClick = { foodId ->
                                // 선택된 아이템으로 스크롤
                                coroutineScope.launch {
                                    foodIndexMap[foodId]?.let { index ->
                                        // 아이템 위치로 스크롤 (중앙 정렬)
                                        lazyRowState.animateScrollToItem(
                                            index = index,
                                            scrollOffset = -lazyRowState.layoutInfo.viewportSize.width / 2 + lazyRowState.layoutInfo.visibleItemsInfo.firstOrNull()?.size?.div(
                                                2
                                            )!!
                                        )
                                    }
                                }
                                onClick(foodId)


                            }, isSelected = selectedFoodId == foodSimilars[index].foodId
                        )
                    }
                }

                is ResultState.Loading -> {
                    // 로딩 상태 처리
                }

                is ResultState.Error -> {
                    // 에러 상태 처리
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FoodEditScreenPreview() {
    FoodonTheme {
        FoodEditScreen(onBackClick = {}, viewModel = hiltViewModel())
    }
}