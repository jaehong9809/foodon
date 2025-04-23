package com.swallaby.foodon.presentation.calendar.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.font.NotoTypography
import com.swallaby.foodon.core.ui.theme.font.SpoqaHanSansNeo
import com.swallaby.foodon.core.ui.theme.font.SpoqaTypography
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarDayItem(
    date: LocalDate,
    kcal: Int?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .size(48.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 원형 진행도 등 추가 가능
        Text(
            text = "${date.dayOfMonth}",
            style = if (isSelected) SpoqaTypography.SpoqaMedium13 else SpoqaTypography.SpoqaMedium13
        )
        kcal?.let {
            Text(
                text = "$it",
                style = SpoqaTypography.SpoqaNormal11
            )
        }
    }
}
