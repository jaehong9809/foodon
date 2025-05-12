package com.swallaby.foodon.presentation.calendar.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.core.util.DateUtil.getDateShape
import com.swallaby.foodon.domain.calendar.model.CalendarItem
import com.swallaby.foodon.domain.calendar.model.CalendarType
import com.swallaby.foodon.presentation.calendar.viewmodel.CalendarUiState
import org.threeten.bp.LocalDate

@Composable
fun CalendarBody(
    calendarItemMap: Map<String, CalendarItem>,
    uiState: CalendarUiState,
    onDateSelected: (LocalDate) -> Unit
) {

    val calendarType = CalendarType.values()[uiState.selectedTabIndex]
    val yearMonth = uiState.currentYearMonth

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
            val isSelectedWeek = week == uiState.selectedWeekIndex

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
                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = if (calendarType == CalendarType.MEAL) 82.dp else 68.dp),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                if (calendarType == CalendarType.RECOMMENDATION && isSelectedWeek) {
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
                                            if (calendarType == CalendarType.RECOMMENDATION) {
                                                Modifier.height(41.dp)
                                            } else {
                                                Modifier
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CalendarDayItem(
                                        calendarType = calendarType,
                                        calendarItem = calendarItem,
                                        date = date,
                                        today = uiState.today,
                                        isSelected = uiState.selectedDate == date,
                                        onClick = { onDateSelected(date) }
                                    )
                                }
                            }
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
