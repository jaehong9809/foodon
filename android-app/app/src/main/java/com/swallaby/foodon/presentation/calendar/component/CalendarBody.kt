package com.swallaby.foodon.presentation.calendar.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.core.util.DateUtil.getDateShape
import com.swallaby.foodon.domain.calendar.model.CalendarItem
import com.swallaby.foodon.domain.calendar.model.CalendarType
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth

@Composable
fun CalendarBody(
    calendarItemMap: Map<String, CalendarItem>,
    type: CalendarType = CalendarType.MEAL,
    yearMonth: YearMonth,
    selectedDate: LocalDate?,
    today: LocalDate,
    selectedWeekIndex: Int,
    onDateSelected: (LocalDate) -> Unit
) {
    val firstDayOfMonth = yearMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
    val daysInMonth = yearMonth.lengthOfMonth()

    // 달력 그리기
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        var dayCounter = 1

        repeat(6) { week ->
            val isSelectedWeek = week == selectedWeekIndex

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (dayOfWeek in 0..6) {
                    val day = when {
                        week == 0 && dayOfWeek < firstDayOfWeek -> null
                        else -> dayCounter
                    }

                    if (day != null && day <= daysInMonth) {
                        val date = yearMonth.atDay(day)
                        val calendarItem = calendarItemMap[date.toString()]
                        val dayOfWeekFromDate = date.dayOfWeek.value % 7
                        val shape = getDateShape(dayOfWeekFromDate, day, daysInMonth, isSelectedWeek)

                        // 캘린더 안에 내용
                        CalendarDayBox(
                            modifier = Modifier.weight(1f),
                            date = date,
                            calendarItem = calendarItem,
                            type = type,
                            isSelected = selectedDate == date,
                            today = today,
                            shape = shape,
                            isSelectedWeek = isSelectedWeek,
                            onDateSelected = onDateSelected
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

@Composable
private fun CalendarDayBox(
    modifier: Modifier = Modifier,
    date: LocalDate,
    calendarItem: CalendarItem?,
    type: CalendarType,
    isSelected: Boolean,
    today: LocalDate,
    shape: Shape,
    isSelectedWeek: Boolean,
    onDateSelected: (LocalDate) -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = if (type == CalendarType.RECOMMENDATION) 68.dp else 82.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            if (type == CalendarType.RECOMMENDATION && isSelectedWeek) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(41.dp)
                        .background(
                            color = WB500.copy(alpha = 0.1f),
                            shape = shape
                        )
                )
            }

            Box(
                modifier = Modifier
                    .then(
                        if (type == CalendarType.RECOMMENDATION) {
                            Modifier.height(41.dp)
                        } else {
                            Modifier
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                CalendarDayItem(
                    calendarItem = calendarItem,
                    type = type,
                    date = date,
                    today = today,
                    isSelected = isSelected,
                    onClick = { onDateSelected(date) }
                )
            }
        }
    }
}
