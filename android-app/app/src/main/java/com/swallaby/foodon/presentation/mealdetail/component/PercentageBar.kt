package com.swallaby.foodon.presentation.mealdetail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography.SpoqaMedium14

@Composable
fun PercentageBar(
    modifier: Modifier = Modifier,
    percentage: Float = 0.5f,
) {
    // todo 수정 예정
//    val density = LocalDensity.current
//    val heightInPx = with(density) { 24.dp.toPx() }
//    SubcomposeLayout(modifier = modifier.padding(horizontal = 8.dp)) { constraints ->
//        val text = ((percentage * 100).toInt()).toString() + "%"
//        val textPlaceable = subcompose("percentageText") {
//            Text(
//                text = text,
//                style = SpoqaMedium14.copy(color = Color.White)
//            )
//        }.first().measure(constraints)
//        Log.d("PercentageBar", "textPlaceable.width: ${textPlaceable.width} , constraints.maxWidth: ${constraints.maxWidth}")
//
//        if (textPlaceable.width <= constraints.maxWidth) {
//            layout(constraints.maxWidth, constraints.maxHeight) {
//                // 디버깅용 로그
//                Log.d("PercentageBar", "24.dp를 px로 변환: $heightInPx")
//                Log.d(
//                    "PercentageBar",
//                    "constraints.maxHeight: ${constraints.maxHeight}, textPlaceable.height: ${textPlaceable.height}"
//                )
//                textPlaceable.place(0, (constraints.maxHeight - textPlaceable.height) / 2)
//            }
//        } else {
//            layout(constraints.maxWidth, constraints.maxHeight) {
//
//            }
//        }
//
//    }

    Box(
        modifier = modifier.padding(horizontal = 8.dp),
        contentAlignment = androidx.compose.ui.Alignment.CenterStart
    ) {
        Text(
            ((percentage * 100).toInt()).toString() + "%",
            style = SpoqaMedium14.copy(color = Color.White)
        )
    }
}

@Preview
@Composable
fun PercentageBarPreview() {
    PercentageBar(
        percentage = .5f
    )
}