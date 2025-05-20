package com.swallaby.foodon.presentation.mealdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography.SpoqaMedium14


@Composable
fun PercentageBar(
    modifier: Modifier = Modifier,
    percentage: Float = 0.5f,
    animatedPercentage: Float = percentage,
    backgroundColor: Color = Color.Blue,
) {
    val density = LocalDensity.current
    val paddingPx = with(density) {
        16.dp.toPx()
    }
    val textStyle = SpoqaMedium14.copy(color = Color.White)
    val percentageText = "${(percentage * 100).toInt()}%"

    val textMeasurer = rememberTextMeasurer()

    val textSize = textMeasurer.measure(
        text = percentageText, style = textStyle
    ).size

    var boxWidth by remember { mutableStateOf(0) }

    Box(modifier = modifier.onGloballyPositioned { coordinates ->
        boxWidth = coordinates.size.width - paddingPx.toInt()
    }) {
        // 배경 바 (전체 영역)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
        )

        // 애니메이션되는 비율 바
        Box(
            modifier = Modifier
                .fillMaxHeight()
                // width를 animatedPercentage에 따라 설정
                .fillMaxWidth(fraction = if (percentage > 0) animatedPercentage / percentage else 0f)
                .background(
                    color = backgroundColor, RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 8.dp), contentAlignment = Alignment.CenterStart
        ) {
            val textWidthWithPadding = textSize.width + 16.dp.value

            // 애니메이션 진행도에 따라 텍스트의 알파값 조정
            val textAlpha = if (percentage == 0f) 0f else {
                val ratio = animatedPercentage / percentage
                if (ratio > 0.5f) ratio else 0f
            }

            if (boxWidth > textWidthWithPadding) {
                Text(
                    text = percentageText,
                    style = textStyle.copy(color = textStyle.color.copy(alpha = textAlpha)),
                    maxLines = 1,
                )
            }
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