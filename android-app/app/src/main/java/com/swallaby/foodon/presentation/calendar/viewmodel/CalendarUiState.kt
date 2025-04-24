package com.swallaby.foodon.presentation.calendar.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.calendar.model.CalendarItem
import org.threeten.bp.LocalDate

data class CalendarUiState(
    val calendarState: ResultState<List<CalendarItem>> = ResultState.Loading,
    val selectedDate: LocalDate = LocalDate.now(),
    val today: LocalDate = LocalDate.now()
): UiState
