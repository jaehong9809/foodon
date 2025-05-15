package com.swallaby.foodon.presentation.calendar.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.calendar.model.CalendarItem
import com.swallaby.foodon.domain.calendar.model.RecommendFood
import com.swallaby.foodon.domain.calendar.model.UserWeight

data class CalendarUiState(
    val calendarResult: ResultState<List<CalendarItem>> = ResultState.Loading,
    val weightResult: ResultState<UserWeight> = ResultState.Loading,
    val recommendFoods: ResultState<List<RecommendFood>> = ResultState.Loading,

    val isInitialLoaded: Boolean = false,
    val selectedTabIndex: Int = 0,
    val selectedWeekIndex: Int = 0,
): UiState
