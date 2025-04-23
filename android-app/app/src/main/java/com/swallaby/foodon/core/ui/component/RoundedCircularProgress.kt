package com.swallaby.foodon.core.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.Bkg05
import com.swallaby.foodon.core.ui.theme.WB400

@Composable
fun RoundedCircularProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    trackColor: Color = Bkg05,
    progressColor: Color = WB400,
    strokeWidth: Dp = 2.dp
) {
    val stroke = with(LocalDensity.current) { strokeWidth.toPx() }

    Canvas(modifier = modifier) {
        val sweepAngle = progress.coerceIn(0f, 1f) * 360f

        // 배경 트랙
        drawArc(
            color = trackColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )

        // 진행된 부분
        drawArc(
            color = progressColor,
            startAngle = -90f,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
    }
}