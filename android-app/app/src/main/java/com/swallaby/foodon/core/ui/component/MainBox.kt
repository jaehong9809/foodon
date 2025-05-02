package com.swallaby.foodon.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.font.NotoTypography

@Composable
fun CommonBox(
    content: String,
    bgColor: Color,
    textColor: Color,
    textStyle: TextStyle = NotoTypography.NotoMedium12,
    horizontalPadding: Dp = 8.dp,
    height: Dp = 24.dp
) {
    Column(
        modifier = Modifier
            .wrapContentWidth()
            .height(height)
            .background(color = bgColor, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = horizontalPadding)
    ) {
        Text(
            text = content,
            color = textColor,
            style = textStyle,
        )
    }
}