package com.swallaby.foodon.presentation.foodEdit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.Bkg04
import com.swallaby.foodon.core.ui.theme.G400
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
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
    textModifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
) {

    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = Integer.MAX_VALUE
    val listScrollMiddle = listScrollCount / 2
    val listStartIndex =
        listScrollMiddle - listScrollMiddle % items.size - visibleItemsMiddle + startIndex

    fun getItem(index: Int) = items[index % items.size]

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

//    val itemHeightPixels = remember { mutableIntStateOf(0) }
    val itemHeightDp = 48.dp// pixelsToDp(itemHeightPixels.value)

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }.map { index -> getItem(index + visibleItemsMiddle) }
            .distinctUntilChanged().collect { item -> state.selectedItem = item }
    }



    Box(modifier = modifier.height(200.dp)) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeightDp * visibleItemsCount)
        ) {
            items(listScrollCount) { index ->
                Box(
                    modifier = Modifier.height(48.dp)
//                        .onSizeChanged { size ->
//                            itemHeightPixels.value = size.height
//                        }
                    , contentAlignment = Alignment.Center
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
    val timeValues = remember { (0..59).map { it.toString() } }
    val hourValues = remember { (1..12).map { it.toString() } }
    val valuesPickerState = rememberPickerState()
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
                state = valuesPickerState,
                items = timeValues,
                visibleItemsCount = 5,
                modifier = modifier.weight(1f),
                textStyle = SpoqaTypography.SpoqaBold18,
            )
            Picker(
                state = valuesPickerState,
                items = hourValues,
                visibleItemsCount = 5,
                modifier = modifier.weight(1f),
                textStyle = SpoqaTypography.SpoqaBold18,
            )
            Picker(
                state = valuesPickerState,
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