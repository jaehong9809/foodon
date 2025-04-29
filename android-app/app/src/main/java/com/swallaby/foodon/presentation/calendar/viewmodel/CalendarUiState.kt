package com.swallaby.foodon.presentation.calendar.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.calendar.model.CalendarItem
import com.swallaby.foodon.domain.calendar.model.RecommendFood
import com.swallaby.foodon.domain.calendar.model.UserWeight
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth

data class CalendarUiState(
    val calendarResult: ResultState<List<CalendarItem>> = ResultState.Loading,
    val weightResult: ResultState<UserWeight> = ResultState.Loading,
    val recommendFoods: ResultState<List<RecommendFood>> = ResultState.Loading,
    val selectedDate: LocalDate = LocalDate.now(),
    val today: LocalDate = LocalDate.now(),
    val selectedTabIndex: Int = 0,
    val currentYearMonth: YearMonth = YearMonth.now(),
    val selectedWeekIndex: Int = 0
): UiState
