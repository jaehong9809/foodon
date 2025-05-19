package com.swallaby.foodon.presentation.mealdetail.component

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography.SpoqaMedium14


@Composable
fun PercentageBar(
    modifier: Modifier = Modifier,
    percentage: Float = 0.5f,
) {
    val textStyle = SpoqaMedium14.copy(color = Color.White)
    val percentageText = "${(percentage * 100).toInt()}%"

    val textMeasurer = rememberTextMeasurer()

    val textSize = textMeasurer.measure(
        text = percentageText, style = textStyle
    ).size

    var boxWidth by remember { mutableStateOf(0) }

    Box(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .onGloballyPositioned { coordinates ->
                boxWidth = coordinates.size.width
            }, contentAlignment = Alignment.CenterStart
    ) {
        val textWidthWithPadding = textSize.width + 16.dp.value

        if (boxWidth > textWidthWithPadding) {
            Text(
                text = percentageText, style = textStyle, maxLines = 1, overflow = TextOverflow.Clip
            )
        }
    }
}

@Preview
@Composable
fun PercentageBarPreview() {
    PercentageBar(
        percentage = .5f
    )
}