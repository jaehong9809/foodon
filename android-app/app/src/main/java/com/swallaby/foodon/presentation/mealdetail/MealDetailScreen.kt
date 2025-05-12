package com.swallaby.foodon.presentation.mealdetail

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.swallaby.foodon.R
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.component.BackIconImage
import com.swallaby.foodon.core.ui.component.CommonWideButton
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.core.ui.theme.FoodonTheme
import com.swallaby.foodon.domain.food.model.MealItem
import com.swallaby.foodon.presentation.foodedit.component.ScrollTimePicker
import com.swallaby.foodon.presentation.mealdetail.component.FoodInfoComponent
import com.swallaby.foodon.presentation.mealdetail.component.FoodLabelButton
import com.swallaby.foodon.presentation.mealdetail.component.NutritionalIngredientsComponent
import com.swallaby.foodon.presentation.mealdetail.viewmodel.MealEditEvent
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
    onNavigateMain: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is MealEditEvent.NavigateToMain -> {
                    onNavigateMain()
                }

                is MealEditEvent.ShowErrorMessage -> {
                    Toast.makeText(context, event.errorMessageRes, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

//    DisposableEffect(Unit) {
//        onDispose {
//            viewModel.destroyMeal()
//        }
//    }

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    when (uiState.mealEditState) {
        is ResultState.Loading -> {
            LoadingState()
        }

        is ResultState.Error -> {
            ErrorState(stringResource(((uiState.mealEditState as ResultState.Error).messageRes)))
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
                    // 음식 이미지와 음식 이름 버튼 영역
                    MealImageWithFoodLabels(
                        mealItems = mealInfo.mealItems,
                        onFoodClick = onFoodClick,
                        imageUri = mealInfo.imageUri
                    )

                    // 영양소 정보 컴포넌트
                    NutritionalIngredientsComponent(
                        modifier = modifier,
                        mealType = mealInfo.mealTimeType,
                        mealTime = mealInfo.mealTime,
                        totalCarbs = mealInfo.totalCarbs,
                        totalFat = mealInfo.totalFat,
                        totalKcal = mealInfo.totalKcal,
                        totalProtein = mealInfo.totalProtein,
                        onMealTypeClick = viewModel::updateMealType,
                        onTimeClick = { showBottomSheet = true })

                    // 구분선
                    Spacer(
                        modifier = Modifier
                            .height(8.dp)
                            .fillMaxWidth()
                            .background(Bkg04)
                    )

                    // 음식 정보 컴포넌트
                    FoodInfoComponent(
                        onClick = onFoodClick,
                        foods = mealInfo.mealItems,
                        imageUri = mealInfo.imageUri,
                        onDelete = viewModel::deleteFood
                    )
                }

                CommonWideButton(
                    modifier.padding(horizontal = 24.dp),
                    text = stringResource(R.string.btn_record_complete),
                    onClick = viewModel::recordMeal
                )
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
                    // todo 이미지 파일
                    var selectedTime by remember { mutableStateOf("08:00") }
                    val times = mealInfo.mealTime.split(":")

                    val selectedAmPmIndex by remember { mutableIntStateOf(if (times[0].toInt() < 12) 0 else 1) }
                    val selectedHourIndex by remember { mutableIntStateOf(times[0].toInt() % 12 - 1) }
                    val selectedMinIndex by remember { mutableIntStateOf(times[1].toInt()) }

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


@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(message: String) {
    // 에러 상태 UI 구현
}


@Composable
fun MealImageWithFoodLabels(
    modifier: Modifier = Modifier,
    imageUri: Uri?,
    mealItems: List<MealItem>, onFoodClick: (foodId: Long) -> Unit,
) {
    var originalImageSize by remember { mutableStateOf(Size(0f, 0f)) }
    var isImageLoaded by remember { mutableStateOf(false) }

    Box {
        val imageContentScale = ContentScale.FillBounds
        val context = LocalContext.current
        val imageUrl = imageUri.toString()
//        "https://img.freepik.com/free-photo/top-view-table-full-food_23-2149209253.jpg?semt=ais_hybrid&w=740"

        // 이미지 표시
        AsyncImage(
            model = ImageRequest.Builder(context).data(imageUrl).crossfade(true)
                .listener(onSuccess = { _, result ->
                    val bitmap = (result.drawable as? BitmapDrawable)?.bitmap
                    if (bitmap != null && !isImageLoaded) {
                        originalImageSize = Size(
                            bitmap.width.toFloat(), bitmap.height.toFloat()
                        )
                        isImageLoaded = true
                    }
                }).build(),
            contentDescription = "음식 사진",
            contentScale = imageContentScale,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )

        // 이미지 로드 완료 후 음식 이름 버튼 표시
        if (isImageLoaded) {
            Log.d(
                "MealDetailScreen",
                "Original image size: ${originalImageSize.width}x${originalImageSize.height}"
            )
            DisplayFoodLabels(mealItems, originalImageSize, onFoodClick)
        } else {
            // 로딩 표시
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun DisplayFoodLabels(
    mealItems: List<MealItem>,
    originalImageSize: Size,
    onFoodClick: (foodId: Long) -> Unit,
) {
    mealItems.forEach { mealItem ->
        mealItem.positions.forEach { position ->
            // position.x와 position.y가 픽셀 단위인 경우 (주석대로)
            // 픽셀을 비율로 변환
            val relativeX = position.x / originalImageSize.width
            val relativeY = position.y / originalImageSize.height

            // 부분 이미지의 너비와 높이 (비율)
            val partialWidth = position.width  // 이미 비율이라고 가정
            val partialHeight = position.height  // 이미 비율이라고 가정

            // 부분 이미지의 중앙 좌표 (비율)
            val centerX = relativeX + (partialWidth / 2)
            val centerY = relativeY + (partialHeight / 2)

            FoodLabelButton(
                position = position,
                originalImageSize = originalImageSize,
                centerPosition = Size(centerX.toFloat(), centerY.toFloat()),  // 중앙 좌표 전달
                foodName = mealItem.foodName,
                onClick = { onFoodClick(mealItem.foodId) })
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
fun MealDetailScreenPreview() {
    FoodonTheme {
        MealDetailScreen(
            viewModel = hiltViewModel(),
            modifier = Modifier,
            onBackClick = {},
            onFoodClick = {},
        )
    }
}

