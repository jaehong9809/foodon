package com.swallaby.foodon.presentation.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.domain.calendar.model.CalendarItem
import com.swallaby.foodon.domain.calendar.model.CalendarType
import com.swallaby.foodon.presentation.calendar.component.CalendarDayItem
import com.swallaby.foodon.presentation.calendar.viewmodel.CalendarUiState
import org.threeten.bp.LocalDate

@Composable
fun MainCalendarPager(
    pagerState: PagerState,
    currentWeekStart: LocalDate,
    mealItemMap: Map<String, CalendarItem>,
    calendarUiState: CalendarUiState,
    onDateSelected: (LocalDate) -> Unit,
) {

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
    ) { page ->

        val offsetFromCenter = page - 1
        val weekStartDate = currentWeekStart.plusWeeks(offsetFromCenter.toLong())
        val week = (0..6).map { weekStartDate.plusDays(it.toLong()) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            week.forEach { date ->
                if (date != null) {
                    val calendarItem = mealItemMap[date.toString()]

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 70.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        CalendarDayItem(
                            calendarType = CalendarType.MEAL,
                            calendarItem = calendarItem,
                            date = date,
                            today = calendarUiState.today,
                            isSelected = calendarUiState.selectedDate == date,
                            onClick = { onDateSelected(date) }
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

            }
        }
    }
}
