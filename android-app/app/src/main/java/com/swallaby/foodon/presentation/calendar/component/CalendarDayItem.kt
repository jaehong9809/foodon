package com.swallaby.foodon.presentation.calendar.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isSelected) WB500 else Color.Transparent,
                    shape = CircleShape
                )
                .padding(8.dp)
        ) {
            Text(
                text = "${date.dayOfMonth}",
                style = if (isSelected) {
                    SpoqaTypography.SpoqaMedium13.copy(color = MainWhite)
                } else {
                    SpoqaTypography.SpoqaMedium13.copy(color = Color.Black)
                },
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // 칼로리 표시
        kcal?.let {
            Text(
                text = "$it kcal",
                style = SpoqaTypography.SpoqaNormal11,
            )
        }
    }
}


