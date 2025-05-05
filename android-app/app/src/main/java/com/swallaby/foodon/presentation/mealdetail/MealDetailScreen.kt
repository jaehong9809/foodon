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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.swallaby.foodon.R
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.component.BackIconImage
import com.swallaby.foodon.core.ui.component.CommonWideButton
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.core.ui.theme.FoodonTheme
import com.swallaby.foodon.presentation.foodedit.component.ScrollTimePicker
import com.swallaby.foodon.presentation.mealdetail.component.FoodInfoComponent
import com.swallaby.foodon.presentation.mealdetail.component.NutritionalIngredientsComponent
import com.swallaby.foodon.presentation.mealdetail.viewmodel.MealEditViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailScreen(
    viewModel: MealEditViewModel,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onFoodClick: (foodId: Long) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    when (uiState.mealEditState) {
        is ResultState.Loading -> {
            // 로딩 중 UI 표시
        }

        is ResultState.Error -> {
            // 에러 처리
            val message = (uiState.mealEditState as ResultState.Error).messageRes
        }

        is ResultState.Success -> {
            // 성공 시 UI 표시
            val mealInfo = (uiState.mealEditState as ResultState.Success).data

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
                    NutritionalIngredientsComponent(
                        modifier = modifier,
                        mealType = uiState.mealType,
                        mealTime = uiState.mealTime,
                        totalCarbs = mealInfo.totalCarbs,
                        totalFat = mealInfo.totalFat,
                        totalKcal = mealInfo.totalKcal,
                        totalProtein = mealInfo.totalProtein,
                        onMealTypeClick = viewModel::updateMealType,
                        onTimeClick = {
                            scope.launch {
                                showBottomSheet = true
                            }
                        })
                    Spacer(
                        modifier
                            .height(8.dp)
                            .fillMaxWidth()
                            .background(Bkg04)
                    )
                    FoodInfoComponent(
                        onClick = onFoodClick,
                        foods = mealInfo.mealItems,
                        onDelete = viewModel::deleteFood
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
                    var selectedTime by remember { mutableStateOf("08:00") }
                    val times = uiState.mealTime.split(":")

                    val selectedAmPmIndex by remember { mutableStateOf(if (times[0].toInt() < 12) 0 else 1) }
                    val selectedHourIndex by remember { mutableStateOf(times[0].toInt() % 12 - 1) }
                    val selectedMinIndex by remember { mutableStateOf(times[1].toInt()) }

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
                        ScrollTimePicker(
                            initTimeIndex = selectedMinIndex,
                            initHourIndex = selectedHourIndex,
                            initAmPmIndex = selectedAmPmIndex,
                            onTimeChanged = {
                                selectedTime = it
                            })

                        CommonWideButton(
                            modifier = Modifier.padding(horizontal = 24.dp),
                            text = "확인",
                            onClick = {
                                viewModel.updateMealTime(selectedTime)
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
        MealDetailScreen(
            viewModel = MealEditViewModel(),
            modifier = Modifier,
            onBackClick = {},
            onFoodClick = {},
        )
    }
}

