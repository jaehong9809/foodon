package com.swallaby.foodon.presentation.calendar.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.core.result.ResultState
import org.threeten.bp.LocalDate

data class CalendarUiState(
    val calendarState: ResultState<List<Any>> = ResultState.Loading,
    val selectedDate: LocalDate = LocalDate.now(),
    val today: LocalDate = LocalDate.now()
): UiState
