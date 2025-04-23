package com.swallaby.foodon.presentation.calendar.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.calendar.model.Meal
import com.swallaby.foodon.domain.user.model.User

data class CalendarUiState(
    val calendarState: ResultState<List<Any>> = ResultState.Loading
): UiState
