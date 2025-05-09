package com.swallaby.foodon.core.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.Bkg05
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.G900
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import com.swallaby.foodon.core.util.StringUtil.formatKcal
import com.swallaby.foodon.domain.food.model.Nutrition

@Composable
fun CalorieProgressBar(
    modifier: Modifier = Modifier,
    nutrients: List<Nutrition> = emptyList(),
    goal: Int = 0,
    consumed: Int = 0,
    strokeWidth: Dp = 12.dp
) {
    val startAngle = 165f
    val sweepAngle = 210f
    val backgroundColor = Bkg05

    val stroke = with(LocalDensity.current) { strokeWidth.toPx() }

    val totalKcal = if (goal > 0) consumed.toFloat() / goal.toFloat() else 0f
    val limitedKcalRatio = totalKcal.coerceAtMost(1f)

    Box(
        modifier = modifier.height(160.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = size.minDimension / 2
            val arcSize = Size(radius * 2, radius * 2)
            val topLeft = Offset(
                (size.width - radius * 2) / 2,
                (size.height - radius * 2) / 2
            )

            // 배경 반원
            drawArc(
                color = backgroundColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = stroke, cap = StrokeCap.Round),
                size = arcSize,
                topLeft = topLeft
            )

            // 누적된 진행각
            var currentAngle = startAngle
            nutrients.forEach { nutrient ->
                val segmentSweep = sweepAngle * nutrient.ratio.coerceAtMost(limitedKcalRatio)
                drawArc(
                    color = nutrient.nutritionType.color,
                    startAngle = currentAngle,
                    sweepAngle = segmentSweep,
                    useCenter = false,
                    style = Stroke(width = stroke, cap = StrokeCap.Round),
                    size = arcSize,
                    topLeft = topLeft
                )
                currentAngle += segmentSweep
            }
        }

        // 총 칼로리 정보
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Spacer(modifier = Modifier.height(38.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatKcal(consumed),
                    style = SpoqaTypography.SpoqaBold24.copy(color = G900)
                )

                Text(
                    text = stringResource(R.string.main_kcal_unit),
                    style = SpoqaTypography.SpoqaMedium16.copy(color = G900)
                )
            }

            Text(
                text = stringResource(R.string.main_pager_kcal_unit, formatKcal(goal)),
                style = SpoqaTypography.SpoqaMedium13.copy(color = G700)
            )
        }
    }
}
