package com.swallaby.foodon.presentation.calendar.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth

@Composable
fun CalendarBody(
    yearMonth: YearMonth,
    selectedDate: LocalDate?,
    today: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val firstDayOfMonth = yearMonth.atDay(1)
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7

    Log.d("CalendarBody", "yearMonth: $yearMonth")
    Log.d("CalendarBody", "First Day of Month: $firstDayOfMonth, Days in Month: $daysInMonth, First Day of Week: $firstDayOfWeek")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        var dayCounter = 1

        for (week in 0 until 6) {
            Log.d("CalendarBody", "Week $week: ")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (dayOfWeek in 0..6) {
                    val day = if (week == 0 && dayOfWeek < firstDayOfWeek) {
                        null  // 첫 번째 주의 첫날 이전은 비어있음
                    } else {
                        dayCounter
                    }

                    if (day != null && day <= daysInMonth) {
                        val date = yearMonth.atDay(day)

                        Box(
                            modifier = Modifier
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            CalendarDayItem(
                                date = date,
                                today = today,
                                isSelected = selectedDate == date,
                                onClick = { onDateSelected(date) }
                            )
                        }

                        dayCounter++
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

