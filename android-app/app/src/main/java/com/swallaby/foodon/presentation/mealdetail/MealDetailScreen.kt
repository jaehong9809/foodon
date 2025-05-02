package com.swallaby.foodon.presentation.mealdetail


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.component.BackIconImage
import com.swallaby.foodon.core.ui.component.CommonWideButton
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.core.ui.theme.FoodonTheme
import com.swallaby.foodon.domain.food.model.MealInfo
import com.swallaby.foodon.domain.food.model.MealItem
import com.swallaby.foodon.domain.food.model.MealType
import com.swallaby.foodon.domain.food.model.NutrientInfo
import com.swallaby.foodon.domain.food.model.Position
import com.swallaby.foodon.presentation.foodedit.component.ScrollTimePicker
import com.swallaby.foodon.presentation.mealdetail.component.FoodInfoComponent
import com.swallaby.foodon.presentation.mealdetail.component.NutritionalIngredientsComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailScreen(
    modifier: Modifier = Modifier,
    mealId: Long = 0,
    onBackClick: () -> Unit,
    onFoodClick: (foodId: Long) -> Unit,
    onFoodDeleteClick: (foodId: Long) -> Unit,
) {


    val now = LocalDateTime.now()
    Log.d("Picker", "now: $now")
    var selectedTime by remember { mutableStateOf("08:00") }
    val selectedAmPm by remember { mutableStateOf(if (now.hour < 12) 0 else 1) }
    val selectedHour by remember { mutableStateOf(now.hour % 12 - 1) }
    val selectedMin by remember { mutableStateOf(now.minute) }

    val foods = createDummyMealInfo()
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scrollState = rememberScrollState()

    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://img.freepik.com/free-photo/top-view-table-full-food_23-2149209253.jpg?semt=ais_hybrid&w=740")
                    .crossfade(true).listener(onError = { _, result ->
                        Log.e("ImageLoading", "Error loading image: ${result.throwable}")
                    }, onSuccess = { _, _ ->
                        Log.d("ImageLoading", "Image loaded successfully")
                    }).build(),
                contentDescription = "음식 사진",
                modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
//                contentScale = ContentScale.Crop,
                contentScale = ContentScale.FillBounds,
                error = painterResource(R.drawable.icon_time), // 에러 시 표시할 이미지
                placeholder = painterResource(R.drawable.icon_search) // 로딩 중 표시할 이미지
            )

            // todo icon_time 의 크기가 피그마와 일치하지 않음
            //  피그마보다 좀 더 작음
            NutritionalIngredientsComponent(modifier = modifier,
                mealType = MealType.BREAKFAST,
                mealTime = selectedTime,
                totalCarbs = 50,
                totalFat = 200,
                totalKcal = 100,
                totalProtein = 70,
                onMealTypeClick = {},
                onTimeClick = {
                    scope.launch {
                        showBottomSheet = true
                    }
                })


            Spacer(
                modifier
                    .height(8.dp)
                    .background(Bkg04)
            )
            FoodInfoComponent(
                onClick = onFoodClick,
                foods = foods.mealItems,
                onDelete = onFoodDeleteClick
            )
        }

        CommonWideButton(modifier.padding(horizontal = 24.dp), text = "기록 완료", onClick = {})
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackIconImage(modifier = Modifier, onBackClick)
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(dragHandle = null, sheetState = sheetState, onDismissRequest = {
            showBottomSheet = false
        }) {


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
                ScrollTimePicker(initTimeIndex = selectedMin,
                    initHourIndex = selectedHour,
                    initAmPmIndex = selectedAmPm,
                    onTimeChanged = {
                        selectedTime = it
                    })

                CommonWideButton(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = "확인",
                    onClick = {
                        dismissModalBottomSheet(
                            scope = scope,
                            sheetState = sheetState,
                            callback = {
                                Log.d("Picker", "selectedTime: $selectedTime")
                                showBottomSheet = false
                            },
                        )
                    },
                )

            }

        }
    }
}

@ExperimentalMaterial3Api
fun dismissModalBottomSheet(
    scope: CoroutineScope,
    sheetState: SheetState,
    callback: () -> Unit,
) {
    scope.launch {
        sheetState.hide()
    }.invokeOnCompletion {
        if (!sheetState.isVisible) {
            callback()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FoodDetailScreenPreview() {
    FoodonTheme {
        MealDetailScreen(modifier = Modifier,
            onBackClick = {},
            onFoodClick = {},
            onFoodDeleteClick = {})
    }
}

fun createDummyMealInfo(): MealInfo = MealInfo(
    imageUrl = "https://example.com/breakfast.jpg",
    mealTime = "2025-05-02 07:30",
    mealTimeType = "BREAKFAST",
    totalCarbs = 45,
    totalFat = 15,
    totalProtein = 20,
    totalKcal = 390,
    mealItems = listOf(
        MealItem(
            type = "PUBLIC",
            foodId = 1001,
            foodName = "계란 프라이",
            unit = "개",
            quantity = 2,
            nutrientInfo = NutrientInfo(
                kcal = 140,
                protein = 12,
                fat = 10,
                carbs = 2,
                sugar = 0,
                fiber = 0,
                sodium = 140,
                cholesterol = 370,
                potassium = 120,
                saturatedFat = 3,
                unsaturatedFat = 7,
                transFat = 0,
                fattyAcid = 5,
                alcohol = 0
            ),
            position = listOf(
                Position(
                    height = 120.0, width = 130.0, x = 50, y = 100
                )
            )
        ), MealItem(
            type = "PUBLIC",
            foodId = 1002,
            foodName = "토스트",
            unit = "조각",
            quantity = 2,
            nutrientInfo = NutrientInfo(
                kcal = 180,
                protein = 6,
                fat = 3,
                carbs = 32,
                sugar = 3,
                fiber = 2,
                sodium = 200,
                cholesterol = 0,
                potassium = 70,
                saturatedFat = 1,
                unsaturatedFat = 2,
                transFat = 0,
                fattyAcid = 1,
                alcohol = 0
            ),
            position = listOf(
                Position(
                    height = 80.0, width = 150.0, x = 200, y = 120
                )
            )
        ), MealItem(
            type = "PUBLIC",
            foodId = 1003,
            foodName = "오렌지 주스",
            unit = "ml",
            quantity = 250,
            nutrientInfo = NutrientInfo(
                kcal = 70,
                protein = 2,
                fat = 2,
                carbs = 11,
                sugar = 9,
                fiber = 1,
                sodium = 5,
                cholesterol = 0,
                potassium = 450,
                saturatedFat = 0,
                unsaturatedFat = 2,
                transFat = 0,
                fattyAcid = 0,
                alcohol = 0
            ),
            position = listOf(
                Position(
                    height = 150.0, width = 70.0, x = 400, y = 80
                )
            )
        )
    )
)