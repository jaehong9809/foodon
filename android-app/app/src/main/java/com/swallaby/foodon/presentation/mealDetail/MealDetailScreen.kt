package com.swallaby.foodon.presentation.mealDetail


import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.component.BackIconImage
import com.swallaby.foodon.core.ui.component.CommonWideButton
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.core.ui.theme.FoodonTheme
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import com.swallaby.foodon.domain.food.model.MealNutrientWithType
import com.swallaby.foodon.domain.food.model.MealType
import com.swallaby.foodon.presentation.mealDetail.component.FoodInfoComponent
import com.swallaby.foodon.presentation.mealDetail.component.NutritionalIngredientsComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailScreen(
    modifier: Modifier = Modifier,
    mealId: Long = 0,
    onBackClick: () -> Unit,
    onFoodClick: (foodId: Long) -> Unit,
) {
    val foods = createDummyMealNutrients()
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

            // 시간 휠 피커
            TimeWheelPicker()

            // todo icon_time 의 크기가 피그마와 일치하지 않음
            //  피그마보다 좀 더 작음
            NutritionalIngredientsComponent(modifier = modifier,
                mealType = MealType.BREAKFAST,
                mealTime = "12:00",
                totalCarbs = 50,
                totalFat = 200,
                totalKcal = 100,
                totalProtein = 70,
                onMealTypeClick = {

                },
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
            FoodInfoComponent(onClick = onFoodClick, foods = foods)
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

                CommonWideButton(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = "확인",
                    onClick = {
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
        MealDetailScreen(modifier = Modifier, onBackClick = {}, onFoodClick = {})
    }
}

private fun createDummyMealNutrients(): List<MealNutrientWithType> {
    return listOf(
        MealNutrientWithType(
            foodId = 1001,
            foodName = "구운 닭 가슴살",
            kcal = 165,
            carbs = 0,
            protein = 31,
            fat = 3,
            fiber = 0,
            sugar = 0,
            sodium = 74,
            cholesterol = 85,
            saturatedFat = 1,
            unsaturatedFat = 2,
            transFat = 0,
            fattyAcid = 0,
            kalium = 256,
            caffeine = 0,
            alcohol = 0,
            unit = "100g",
            type = "LUNCH"
        ), MealNutrientWithType(
            foodId = 1002,
            foodName = "현미밥",
            kcal = 130,
            carbs = 28,
            protein = 3,
            fat = 1,
            fiber = 2,
            sugar = 0,
            sodium = 5,
            cholesterol = 0,
            saturatedFat = 0,
            unsaturatedFat = 1,
            transFat = 0,
            fattyAcid = 0,
            kalium = 80,
            caffeine = 0,
            alcohol = 0,
            unit = "100g",
            type = "LUNCH"
        ), MealNutrientWithType(
            foodId = 1003,
            foodName = "삶은 계란",
            kcal = 78,
            carbs = 1,
            protein = 6,
            fat = 5,
            fiber = 0,
            sugar = 0,
            sodium = 62,
            cholesterol = 212,
            saturatedFat = 2,
            unsaturatedFat = 3,
            transFat = 0,
            fattyAcid = 0,
            kalium = 63,
            caffeine = 0,
            alcohol = 0,
            unit = "개(50g)",
            type = "BREAKFAST"
        ), MealNutrientWithType(
            foodId = 1004,
            foodName = "아보카도",
            kcal = 160,
            carbs = 9,
            protein = 2,
            fat = 15,
            fiber = 7,
            sugar = 1,
            sodium = 7,
            cholesterol = 0,
            saturatedFat = 2,
            unsaturatedFat = 13,
            transFat = 0,
            fattyAcid = 0,
            kalium = 485,
            caffeine = 0,
            alcohol = 0,
            unit = "개(100g)",
            type = "BREAKFAST"
        ), MealNutrientWithType(
            foodId = 1005,
            foodName = "연어 스테이크",
            kcal = 208,
            carbs = 0,
            protein = 20,
            fat = 13,
            fiber = 0,
            sugar = 0,
            sodium = 59,
            cholesterol = 55,
            saturatedFat = 3,
            unsaturatedFat = 10,
            transFat = 0,
            fattyAcid = 0,
            kalium = 380,
            caffeine = 0,
            alcohol = 0,
            unit = "100g",
            type = "DINNER"
        ), MealNutrientWithType(
            foodId = 1006,
            foodName = "그릭 요거트",
            kcal = 97,
            carbs = 4,
            protein = 10,
            fat = 5,
            fiber = 0,
            sugar = 4,
            sodium = 36,
            cholesterol = 17,
            saturatedFat = 3,
            unsaturatedFat = 2,
            transFat = 0,
            fattyAcid = 0,
            kalium = 141,
            caffeine = 0,
            alcohol = 0,
            unit = "100g",
            type = "SNACK"
        ), MealNutrientWithType(
            foodId = 1007,
            foodName = "블루베리",
            kcal = 57,
            carbs = 14,
            protein = 1,
            fat = 0,
            fiber = 2,
            sugar = 10,
            sodium = 1,
            cholesterol = 0,
            saturatedFat = 0,
            unsaturatedFat = 0,
            transFat = 0,
            fattyAcid = 0,
            kalium = 77,
            caffeine = 0,
            alcohol = 0,
            unit = "100g",
            type = "SNACK"
        ), MealNutrientWithType(
            foodId = 1008,
            foodName = "시금치 샐러드",
            kcal = 23,
            carbs = 4,
            protein = 3,
            fat = 0,
            fiber = 2,
            sugar = 0,
            sodium = 79,
            cholesterol = 0,
            saturatedFat = 0,
            unsaturatedFat = 0,
            transFat = 0,
            fattyAcid = 0,
            kalium = 558,
            caffeine = 0,
            alcohol = 0,
            unit = "100g",
            type = "DINNER"
        ), MealNutrientWithType(
            foodId = 1009,
            foodName = "바나나",
            kcal = 89,
            carbs = 23,
            protein = 1,
            fat = 0,
            fiber = 3,
            sugar = 12,
            sodium = 1,
            cholesterol = 0,
            saturatedFat = 0,
            unsaturatedFat = 0,
            transFat = 0,
            fattyAcid = 0,
            kalium = 358,
            caffeine = 0,
            alcohol = 0,
            unit = "개(100g)",
            type = "SNACK"
        ), MealNutrientWithType(
            foodId = 1010,
            foodName = "토마토 스파게티",
            kcal = 158,
            carbs = 31,
            protein = 5,
            fat = 2,
            fiber = 2,
            sugar = 5,
            sodium = 227,
            cholesterol = 0,
            saturatedFat = 0,
            unsaturatedFat = 2,
            transFat = 0,
            fattyAcid = 0,
            kalium = 290,
            caffeine = 0,
            alcohol = 0,
            unit = "100g",
            type = "DINNER"
        )
    )
}


@Composable
fun TimeWheelPicker(
    modifier: Modifier = Modifier,
    onTimeSelected: (hour: Int, minute: Int) -> Unit = { _, _ -> },
) {
    // Time range
    val hours = (0..23).toList()
    val minutes = (0..59).toList()

    // Pager states
    val hourPagerState = rememberPagerState(initialPage = 0, pageCount = { 24 })
    val minutePagerState = rememberPagerState(initialPage = 0, pageCount = { 60 })

    // Coroutine scope for pager animations
    val coroutineScope = rememberCoroutineScope()

    val flingBehavior = PagerDefaults.flingBehavior(
        state = hourPagerState,
        // 페이지 변경을 위해 필요한 최소 속도 조절 (기본값보다 높이면 더 빠른 스와이프 필요)
        pagerSnapDistance = PagerSnapDistance.atMost(3),
        // 페이지 위치에서 얼마나 떨어져 있어야 다음/이전 페이지로 이동할지
        snapAnimationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow
        )
    )

    // Selected time
    val selectedHour = remember { mutableStateOf(hours[hourPagerState.currentPage % hours.size]) }
    val selectedMinute =
        remember { mutableStateOf(minutes[minutePagerState.currentPage % minutes.size]) }

    // Effect to update selected time and call callback
    LaunchedEffect(hourPagerState.currentPage, minutePagerState.currentPage) {
        selectedHour.value = hours[hourPagerState.currentPage % hours.size]
        selectedMinute.value = minutes[minutePagerState.currentPage % minutes.size]
        onTimeSelected(selectedHour.value, selectedMinute.value)
    }

    val listColumnState = rememberLazyListState()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 24.dp)
    ) {
        // Highlight selected area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .align(Alignment.Center)
                .background(color = Bkg04, shape = RoundedCornerShape(6.dp))
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Hours wheel
            Box(
                modifier = Modifier.weight(1f), contentAlignment = Alignment.Center
            ) {

                LazyColumn(
                    state = listColumnState, flingBehavior = rememberSnapFlingBehavior(
                        lazyListState = listColumnState,
                        snapPosition = SnapPosition.Center
                    )
                ) {
                    items(hours.size) { page ->
                        val hour = hours[page % hours.size]
                        val isSelected = hour == selectedHour.value
                        Box(
                            modifier = modifier
                                .height(48.dp)
                                .align(Alignment.Center)
                        ) {
                            Text(
                                text = String.format("%02d", hour),
                                textAlign = TextAlign.Center,
                                style = SpoqaTypography.SpoqaBold18.copy(color = G900),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Center)
                            )
                        }

                    }

                }

//                VerticalPager(
//                    state = hourPagerState,
//                    modifier = Modifier.height(200.dp),
//                    contentPadding = PaddingValues(vertical = 76.dp)
//                ) { page ->
//                    val hour = hours[page % hours.size]
//                    val isSelected = hour == selectedHour.value
//                    Text(
//                        text = String.format("%02d", hour),
//                        textAlign = TextAlign.Center,
//                        style = SpoqaTypography.SpoqaBold18.copy(color = G900),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .align(Alignment.Center)
//                    )
//                }
            }

            // Minutes wheel
            Box(
                modifier = Modifier.weight(1f), contentAlignment = Alignment.Center
            ) {
                VerticalPager(
//                    count = Int.MAX_VALUE,
                    state = minutePagerState,
                    modifier = Modifier.height(200.dp),
                    contentPadding = PaddingValues(vertical = 76.dp)
                ) { page ->
                    val minute = minutes[page % minutes.size]
                    val isSelected = minute == selectedMinute.value

                    Text(
                        text = String.format("%02d", minute),
                        fontSize = if (isSelected) 24.sp else 18.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    )
                }
            }
        }
        Box(
            modifier = modifier
                .height(21.dp)
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White, Color.White.copy(alpha = 0f)
                        )
                    )
                )
                .align(Alignment.TopCenter)
        )
        Box(
            modifier = modifier
                .height(28.dp)
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0f), Color.White
                        )
                    )
                )
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun TimePickerExample() {
    var selectedHour by remember { mutableStateOf(0) }
    var selectedMinute by remember { mutableStateOf(0) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select Time",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            TimeWheelPicker(modifier = Modifier.fillMaxWidth(), onTimeSelected = { hour, minute ->
                selectedHour = hour
                selectedMinute = minute
            })

            Text(
                text = "Selected Time: ${String.format("%02d:%02d", selectedHour, selectedMinute)}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}