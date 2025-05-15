package com.swallaby.foodon.presentation.calendar.component

import androidx.compose.foundation.layout.Box
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
import com.swallaby.foodon.domain.calendar.model.CalendarItem
import com.swallaby.foodon.presentation.calendar.model.CalendarStatus
import org.threeten.bp.LocalDate

@Composable
fun CalendarPager(
    pagerState: PagerState,
    calendarItemMap: Map<String, CalendarItem>,
    calendarStatus: CalendarStatus,
    onDateSelected: (LocalDate) -> Unit,
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.TopStart
        ) {
            CalendarBody(
                calendarItemMap = calendarItemMap,
                calendarStatus = calendarStatus,
                onDateSelected = onDateSelected
            )
        }
    }
}