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