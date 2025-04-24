package com.swallaby.foodon.presentation.calendar.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth

@Composable
fun CalendarPager(
    baseYearMonth: YearMonth,
    pagerState: PagerState,
    selectedDate: LocalDate,
    today: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val currentYearMonth = baseYearMonth.plusMonths((pagerState.currentPage - 12).toLong())

    Column {
        CalendarHeader(
            currentYearMonth = currentYearMonth,
            onPreviousMonth = { onPreviousMonth() },
            onNextMonth = { onNextMonth() }
        )

        WeeklyLabel()

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) { page ->
            val yearMonth = baseYearMonth.plusMonths((page - 12).toLong())

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.TopStart
            ) {
                CalendarBody(
                    yearMonth = yearMonth,
                    selectedDate = selectedDate,
                    today = today,
                    onDateSelected = onDateSelected
                )
            }
        }
    }
}
