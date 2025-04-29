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
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.component.LoadingProgress
import com.swallaby.foodon.domain.calendar.model.CalendarItem
import com.swallaby.foodon.domain.calendar.model.CalendarType
import com.swallaby.foodon.presentation.calendar.viewmodel.CalendarUiState
import org.threeten.bp.LocalDate

@Composable
fun CalendarPager(
    pagerState: PagerState,
    calendarType: CalendarType,
    calendarItemMap: Map<String, CalendarItem>,
    uiState: CalendarUiState,
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
                type = calendarType,
                calendarItemMap = calendarItemMap,
                uiState = uiState,
                onDateSelected = onDateSelected
            )

            if (uiState.calendarState is ResultState.Loading) {
                LoadingProgress()
            }
        }
    }
}