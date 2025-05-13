package com.swallaby.foodon.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swallaby.foodon.core.ui.theme.BGWheel
import com.swallaby.foodon.core.ui.theme.G750
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography.SpoqaMedium16
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography.SpoqaMedium32


@Composable
fun NumberWheelPicker(
    valueRange: IntRange,
    initialValue: Int,
    unit: String,
    modifier: Modifier = Modifier,
    tickWidth: Dp = 2.dp,
    majorTickHeight: Dp = 64.dp,
    minorTickHeight: Dp = 48.dp,
    onValueChange: (Int) -> Unit
) {
    val items = remember(valueRange) { valueRange.toList() }
    val initIndex = items.indexOf(initialValue).coerceIn(items.indices)

    val listState = rememberLazyListState(initIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val selectedIndex by remember {
        derivedStateOf { listState.firstVisibleItemIndex }
    }
    val selectedValue by remember {
        derivedStateOf { items.getOrNull(selectedIndex) ?: initialValue }
    }

    // Snapping 패딩 계산
    var parentWidthPx by remember { mutableStateOf(0) }
    val halfPad = with(LocalDensity.current) {
        (parentWidthPx.toDp() / 2) - tickWidth / 2
    }.coerceAtLeast(0.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = selectedValue.toString(),
                style = SpoqaMedium32,
                color = G900
            )
            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = unit,
                style = SpoqaMedium16,
                color = G750,
                modifier = Modifier.offset(y = 2.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(majorTickHeight)
                .onGloballyPositioned { parentWidthPx = it.size.width }
        ) {
            LazyRow(
                state = listState,
                flingBehavior = flingBehavior,
                contentPadding = PaddingValues(horizontal = halfPad),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { value ->
                    val idx = items.indexOf(value)
                    val isSelected = idx == selectedIndex
                    val lineH = if (isSelected) majorTickHeight else minorTickHeight
                    val offsetY = (majorTickHeight - lineH) / 2
                    val lineColor = if (isSelected) WB500 else BGWheel

                    Box(
                        modifier = Modifier
                            .width(tickWidth)
                            .height(lineH)
                            .offset(y = offsetY)
                            .background(lineColor, shape = RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.TopCenter
                    ){}
                }
            }
        }
    }

    LaunchedEffect(selectedValue) {
        onValueChange(selectedValue)
    }
}

@Preview(showBackground = true)
@Composable
fun NumberWheelPickerWithTextPreview() {
    var selected by remember { mutableStateOf(170) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NumberWheelPicker(
            valueRange = 100..300,
            initialValue = selected,
            unit = "cm",
            majorTickHeight = 64.dp,
            minorTickHeight = 48.dp,
            onValueChange = { selected = it }
        )
    }
}