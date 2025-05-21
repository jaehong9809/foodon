package com.swallaby.foodon.presentation.foodedit.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.core.ui.theme.G400
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import com.swallaby.foodon.domain.food.model.PeriodType
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class PickerState(initialSelectedItem: String = "") {
    var selectedItem by mutableStateOf(initialSelectedItem)
}

@Composable
fun rememberPickerState(initialSelectedItem: String = ""): PickerState {
    return remember { PickerState(initialSelectedItem) }
}

@Composable
fun Picker(
    items: List<String>,
    state: PickerState = rememberPickerState(),
    modifier: Modifier = Modifier,
    startIndex: Int = 0,
    visibleItemsCount: Int = 3,
    pickerHeight: Dp = 200.dp,
    itemHeightDp: Dp = 48.dp,
    textStyle: TextStyle = LocalTextStyle.current,
    isLoop: Boolean = true,
//    onChanged: (String) -> Unit,
) {
    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = if (isLoop) Integer.MAX_VALUE else items.size
    val listScrollMiddle = listScrollCount / 2
    val listStartIndex =
        if (isLoop) listScrollMiddle - listScrollMiddle % items.size - visibleItemsMiddle + startIndex else startIndex
    val wheelHeight = itemHeightDp * visibleItemsCount

    fun getItem(index: Int) = items[index % items.size]

    // 아이템을 중앙에 배치하기 위한 패딩 계산
    val itemPadding = (pickerHeight - itemHeightDp) / 2

    val contentPadding = if (isLoop) PaddingValues(0.dp) else PaddingValues(vertical = itemPadding)

    // 처음 Offset 이 맞지 않아 중앙 배치를 위한 Offset 조정
    // ex) item * itemCount => 240dp - 200dp => 40dp / 2 => 20dp
    // item 크기의 절반만큼 조절
    val initialFirstVisibleItemScrollOffset =
        if (isLoop) (((wheelHeight - pickerHeight) / 2) * LocalDensity.current.density).value.toInt() else 0
    Log.d("Picker", "listStartIndex: $listStartIndex")
    Log.d("Picker", "initialFirstVisibleItemScrollOffset: $initialFirstVisibleItemScrollOffset")

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = listStartIndex,
        initialFirstVisibleItemScrollOffset = initialFirstVisibleItemScrollOffset // 적절한 패딩을 사용하므로 오프셋 필요 없음
    )

    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }.map { index -> getItem(index + visibleItemsMiddle) }
            .distinctUntilChanged().collect { item ->
//                onChanged(item)
                state.selectedItem = item
            }
    }

    Box(modifier = modifier.height(pickerHeight)) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(wheelHeight),
            contentPadding = contentPadding // 계산된 패딩 적용
        ) {
            items(listScrollCount) { index ->
                Box(
                    modifier = Modifier.height(itemHeightDp), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getItem(index),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = if (state.selectedItem == getItem(index)) textStyle.copy(color = G900)
                        else textStyle.copy(color = G400),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
fun ScrollTimePicker(
    modifier: Modifier = Modifier,
    initAmPmIndex: Int = 0,
    initHourIndex: Int = 0,
    initTimeIndex: Int = 0,
    onTimeChanged: (String) -> Unit,
) {
    val amPmPickerState =
        rememberPickerState(if (initAmPmIndex == 0) PeriodType.AM.displayName else PeriodType.PM.displayName)
    val hourPickerState = rememberPickerState((initHourIndex + 1).toString() + "시")
    val minPickerState = rememberPickerState(initTimeIndex.toString().padStart(2, '0') + "분")

    // 상태에서 포맷팅된 시간 문자열 파생
    val selectedTime by remember(
        amPmPickerState.selectedItem, hourPickerState.selectedItem, minPickerState.selectedItem
    ) {
        derivedStateOf {
            // "1시", "2시" 등에서 시간 추출
            val hour = hourPickerState.selectedItem.replace("시", "").toIntOrNull() ?: 0

            // "00분", "01분" 등에서 분 추출
            val minute = minPickerState.selectedItem.replace("분", "").toIntOrNull() ?: 0

            // 오후인 경우 24시간 형식으로 변환
            val hour24 =
                if (amPmPickerState.selectedItem == PeriodType.PM.displayName && hour < 12) {
                    hour + 12
                } else if (amPmPickerState.selectedItem == PeriodType.AM.displayName && hour == 12) {
                    0 // 오전 12시는 24시간 형식에서 00:00
                } else {
                    hour
                }

            // "HH:mm" 형식으로 포맷팅
            String.format("%02d:%02d", hour24, minute)
        }
    }

    LaunchedEffect(selectedTime) {
        onTimeChanged(selectedTime)
    }

    val formatHour = stringResource(R.string.format_hour)
    val formatMin = stringResource(R.string.format_min)

    val amPmValues = remember { PeriodType.values().map { it.displayName } }
    val hourValues = remember { (1..12).map { String.format(formatHour, it.toString()) } }
    val minValues = remember {
        (0..59).map {
            String.format(formatMin, it.toString().padStart(2, '0'))
        }
    }


    Box(modifier = modifier.padding(horizontal = 24.dp)) {
        Box(
            modifier = modifier
                .height(48.dp)
                .fillMaxWidth()
                .background(color = Bkg04, shape = RoundedCornerShape(6.dp))
                .align(Alignment.Center)
        )
        Row {
            Picker(
                state = amPmPickerState,
                items = amPmValues,
                visibleItemsCount = 5,
                modifier = modifier.weight(1f),
                textStyle = SpoqaTypography.SpoqaBold18,
                isLoop = false,
                startIndex = initAmPmIndex,
            )
            Picker(
                state = hourPickerState,
                items = hourValues,
                visibleItemsCount = 5,
                modifier = modifier.weight(1f),
                startIndex = initHourIndex,
                textStyle = SpoqaTypography.SpoqaBold18,
            )
            Picker(
                state = minPickerState,
                items = minValues,
                visibleItemsCount = 5,
                modifier = modifier.weight(1f),
                startIndex = initTimeIndex,
                textStyle = SpoqaTypography.SpoqaBold18,
            )

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