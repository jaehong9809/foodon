package com.swallaby.foodon.presentation.calendar.viewmodel

import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.result.toResultState
import com.swallaby.foodon.core.util.DateUtil.getWeekOfMonth
import com.swallaby.foodon.domain.calendar.model.CalendarType
import com.swallaby.foodon.domain.calendar.usecase.GetCalendarUseCase
import com.swallaby.foodon.domain.calendar.usecase.GetRecommendFoodUseCase
import com.swallaby.foodon.domain.calendar.usecase.GetUserWeightUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.temporal.WeekFields
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getCalendarUseCase: GetCalendarUseCase,
    private val getUserWeightUseCase: GetUserWeightUseCase,
    private val getRecommendFoodUseCase: GetRecommendFoodUseCase,
) : BaseViewModel<CalendarUiState>(CalendarUiState()) {

    private val weekFields = WeekFields.of(DayOfWeek.SUNDAY, 1)

    val currentWeekStart: LocalDate
        get() = uiState.value.selectedDate.with(weekFields.dayOfWeek(), 1)

    private var lastCalendarYearMonth: YearMonth? = null
    private var lastRecommendYearMonth: YearMonth? = null
    private var lastRecommendWeek: Int = -1

    fun updateState(block: (CalendarUiState) -> CalendarUiState) {
        _uiState.update(block)
    }

    fun selectDate(date: LocalDate) {
        updateState { it.copy(selectedDate = date) }
    }

    fun selectTab(index: Int) {
        updateState { it.copy(selectedTabIndex = index) }
    }

    fun updateYearMonth(yearMonth: YearMonth) {
        updateState { it.copy(currentYearMonth = yearMonth) }
    }

    fun updateInitialLoaded(state: Boolean) {
        updateState { it.copy(isInitialLoaded = state) }
    }

    fun goToWeek(delta: Int) {
        val today = uiState.value.today
        val targetWeekStart = uiState.value.selectedDate
            .plusWeeks(delta.toLong())
            .with(weekFields.dayOfWeek(), 1)

        val todayWeekStart = today.with(weekFields.dayOfWeek(), 1)

        val newDate = if (targetWeekStart == todayWeekStart) {
            today
        } else {
            targetWeekStart
        }

        updateState {
            it.copy(
                selectedDate = newDate,
                currentYearMonth = YearMonth.from(newDate)
            )
        }
    }

    fun resetToTodayWeek() {
        updateState {
            it.copy(
                selectedDate = uiState.value.today,
                currentYearMonth = YearMonth.from(uiState.value.today)
            )
        }
    }

    fun updateCalendarData(
        calendarType: CalendarType,
        isTabChanged: Boolean = false
    ) {
        val state = uiState.value
        val today = state.today
        val currentYearMonth = state.currentYearMonth
        val isSameMonth = YearMonth.from(today) == currentYearMonth

        val newSelectedDate = when {
            !state.isInitialLoaded || isTabChanged -> state.selectedDate
            isSameMonth -> today
            else -> currentYearMonth.atDay(1)
        }

        updateState {
            it.copy(
                isInitialLoaded = true,
                selectedDate = newSelectedDate
            )
        }

        if (calendarType == CalendarType.RECOMMENDATION) {
            val week = getWeekOfMonth(newSelectedDate)
            updateRecommendation(currentYearMonth, week, indexChanged = true)
        }

        if (isTabChanged && calendarType == CalendarType.WEIGHT) {
            fetchUserWeight()
        }

        fetchCalendarData(calendarType, currentYearMonth)
    }

    fun fetchCalendarData(type: CalendarType, yearMonth: YearMonth) {
        if (lastCalendarYearMonth != yearMonth) {
            lastCalendarYearMonth = yearMonth

            updateState { it.copy(calendarResult = ResultState.Loading) }

            viewModelScope.launch {
                val result = getCalendarUseCase(type, yearMonth)
                updateState { it.copy(calendarResult = result.toResultState()) }
            }
        }
    }

    fun updateRecommendation(yearMonth: YearMonth, week: Int, indexChanged: Boolean = false) {
        if (indexChanged) {
            selectWeek(week - 1)
        }

        fetchRecommendFoods(
            yearMonth = yearMonth,
            week = week
        )
    }

    private fun fetchRecommendFoods(yearMonth: YearMonth, week: Int) {
        if (lastRecommendYearMonth != yearMonth || lastRecommendWeek != week) {
            lastRecommendYearMonth = yearMonth
            lastRecommendWeek = week

            updateState { it.copy(recommendFoods = ResultState.Loading) }

            viewModelScope.launch {
                val result = getRecommendFoodUseCase(yearMonth, week)
                updateState { it.copy(recommendFoods = result.toResultState()) }
            }
        }
    }

    private fun fetchUserWeight() {
        updateState { it.copy(weightResult = ResultState.Loading) }

        viewModelScope.launch {
            val result = getUserWeightUseCase()
            updateState { it.copy(weightResult = result.toResultState()) }
        }
    }

    private fun selectWeek(index: Int) {
        updateState { it.copy(selectedWeekIndex = index) }
    }

}