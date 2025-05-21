package com.swallaby.foodon.core.ui.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// innerBorder
fun Modifier.bottomBorder(
    strokeWidth: Dp = 1.dp,
    color: Color = Border02,
) = this.then(Modifier.drawBehind {
    val borderSize = strokeWidth.toPx()
    val offsetY = size.height - borderSize / 2
    drawLine(
        color = color,
        start = Offset(0f, offsetY),
        end = Offset(size.width, offsetY),
        strokeWidth = borderSize,
    )
})

fun Modifier.crop(
    horizontal: Dp = 0.dp,
    vertical: Dp = 0.dp,
): Modifier = this.layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    fun Dp.toPxInt(): Int = this.toPx().toInt()

    layout(
        placeable.width - (horizontal * 2).toPxInt(),
        placeable.height - (vertical * 2).toPxInt()
    ) {
        placeable.placeRelative(-horizontal.toPx().toInt(), -vertical.toPx().toInt())
    }
}