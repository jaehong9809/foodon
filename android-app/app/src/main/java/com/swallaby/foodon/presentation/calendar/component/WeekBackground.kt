package com.swallaby.foodon.presentation.calendar.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.domain.calendar.model.CalendarType

@Composable
fun WeekBackground(
    weekRange: Pair<Int, Int>?,
    isSelected: Boolean,
    calendarType: CalendarType
) {
    if (calendarType != CalendarType.RECOMMENDATION || weekRange == null) return

    val targetAlpha = if (isSelected) 0.1f else 0f
    val animatedAlpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(300),
        label = "WeekBackgroundAlpha"
    )

    Row(modifier = Modifier.fillMaxWidth()) {
        if (weekRange.first > 0) {
            Spacer(modifier = Modifier.weight(weekRange.first.toFloat()))
        }

        Box(
            modifier = Modifier
                .weight((weekRange.second - weekRange.first + 1).toFloat())
                .height(41.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(WB500.copy(alpha = animatedAlpha))
        )

        if (weekRange.second < 6) {
            Spacer(modifier = Modifier.weight((6 - weekRange.second).toFloat()))
        }
    }
}
