package com.swallaby.foodon.presentation.calendar.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.G700
import com.swallaby.foodon.core.ui.theme.font.NotoTypography

@Composable
fun WeeklyLabel(modifier: Modifier = Modifier) {
    val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 16.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        daysOfWeek.forEach {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = it,
                    style = NotoTypography.NotoMedium13,
                    color = G700
                )
            }
        }
    }
}
