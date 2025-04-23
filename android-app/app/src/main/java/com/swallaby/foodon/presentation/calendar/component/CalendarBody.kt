package com.swallaby.foodon.presentation.calendar.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarBody(
    yearMonth: YearMonth,
    calorieDataMap: Map<LocalDate, Int>,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val firstDayOfMonth = yearMonth.atDay(1)
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7

    Column {
        var dayCounter = 1
        for (week in 0 until 6) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (dayOfWeek in 0..6) {
                    val day = dayCounter.takeIf { week > 0 || dayOfWeek >= firstDayOfWeek }

                    if (day != null && day <= daysInMonth) {
                        val date = yearMonth.atDay(day)
                        CalendarDayItem(
                            date = date,
                            kcal = calorieDataMap[date],
                            isSelected = selectedDate == date,
                            onClick = { onDateSelected(date) }
                        )
                        dayCounter++
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
