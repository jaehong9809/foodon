package com.swallaby.foodon.presentation.calendar.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.MainWhite
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import org.threeten.bp.LocalDate

@Composable
fun CalendarDayItem(
    date: LocalDate,
    kcal: Int?,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .padding(9.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        DayText(date.dayOfMonth, isSelected)

        // 칼로리 표시
        kcal?.let {
            Text(
                text = "$it kcal",
                style = SpoqaTypography.SpoqaNormal11,
            )
        }
    }
}

@Composable
fun DayText(
    day: Int,
    isSelected: Boolean,
) {
    Box(
        modifier = Modifier
            .background(
                color = if (isSelected) WB500 else Color.Transparent,
                shape = CircleShape
            )
            .defaultMinSize(minHeight = 24.dp, minWidth = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.padding(end = 1.dp, top = 0.5.dp, start = 0.5.dp)
        ) {
            Text(
                text = "$day",
                style = if (isSelected) {
                    SpoqaTypography.SpoqaBold13.copy(color = MainWhite)
                } else {
                    SpoqaTypography.SpoqaBold13.copy(color = Color.Black)
                },
                modifier = Modifier.align(Alignment.Center)
            )
        }

    }
}


