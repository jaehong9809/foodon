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

@Composable
fun rememberPickerState() = remember { PickerState() }

class PickerState {
    var selectedItem by mutableStateOf("")
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
) {
    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = if (isLoop) Integer.MAX_VALUE else items.size
    val listScrollMiddle = listScrollCount / 2
    val listStartIndex =
        if (isLoop) listScrollMiddle - listScrollMiddle % items.size - visibleItemsMiddle + startIndex else 0
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
            .distinctUntilChanged().collect { item -> state.selectedItem = item }
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
                        style = if (state.selectedItem == getItem(index)) textStyle.copy(color = G900) else textStyle.copy(
                            color = G400
                        ),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}


@Composable
private fun pixelsToDp(pixels: Int) = with(LocalDensity.current) { pixels.toDp() }

@Composable
fun ScrollTimePicker(modifier: Modifier = Modifier) {

    val formatHour = stringResource(R.string.format_hour)
    val formatMin = stringResource(R.string.format_min)


    val amPmValues = remember { PeriodType.values().map { it.displayName } }
    val hourValues = remember { (1..12).map { String.format(formatHour, it.toString()) } }
    val timeValues = remember {
        (0..59).map {
            String.format(formatMin, it.toString().padStart(2, '0'))
        }
    }

    val amPmPickerState = rememberPickerState()
    val hourPickerState = rememberPickerState()
    val timePickerState = rememberPickerState()
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
                isLoop = false
            )
            Picker(
                state = hourPickerState,
                items = hourValues,
                visibleItemsCount = 5,
                modifier = modifier.weight(1f),
                textStyle = SpoqaTypography.SpoqaBold18,
            )
            Picker(
                state = timePickerState,
                items = timeValues,
                visibleItemsCount = 5,
                modifier = modifier.weight(1f),
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