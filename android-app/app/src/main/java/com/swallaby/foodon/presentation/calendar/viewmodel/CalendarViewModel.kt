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

    fun updateState(block: (CalendarUiState) -> CalendarUiState) {
        _uiState.update(block)
    }

    private val weekFields = WeekFields.of(DayOfWeek.SUNDAY, 1)

    val currentWeekStart: LocalDate
        get() = uiState.value.selectedDate.with(weekFields.dayOfWeek(), 1)

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

    fun selectDate(date: LocalDate) {
        updateState { it.copy(selectedDate = date) }
    }

    fun selectTab(index: Int) {
        updateState { it.copy(selectedTabIndex = index) }
    }

    fun updateYearMonth(yearMonth: YearMonth) {
        updateState { it.copy(currentYearMonth = yearMonth) }
    }

    private fun selectWeek(index: Int) {
        updateState { it.copy(selectedWeekIndex = index) }
    }

    fun updateInitialLoaded(state: Boolean) {
        updateState { it.copy(isInitialLoaded = state) }
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
                selectedDate = newSelectedDate,
                isInitialLoaded = true
            )
        }

        if (calendarType == CalendarType.RECOMMENDATION) {
            val weekIndex = getWeekOfMonth(newSelectedDate)
            updateRecommendation(weekIndex)
        }

        if (isTabChanged && calendarType == CalendarType.WEIGHT) {
            fetchUserWeight()
        }

        fetchCalendarData(calendarType, currentYearMonth.toString())
    }

    fun updateRecommendation(weekIndex: Int) {
        val currentYearMonth = uiState.value.currentYearMonth

        selectWeek(weekIndex)
        fetchRecommendFoods(
            yearMonth = currentYearMonth.toString(),
            week = weekIndex + 1
        )
    }

    fun fetchCalendarData(type: CalendarType, date: String) {
        updateState { it.copy(calendarResult = ResultState.Loading) }

        viewModelScope.launch {
            val result = getCalendarUseCase(type, date)
            updateState { it.copy(calendarResult = result.toResultState()) }
        }
    }

    private fun fetchUserWeight() {
        updateState { it.copy(weightResult = ResultState.Loading) }

        viewModelScope.launch {
            val result = getUserWeightUseCase()
            updateState { it.copy(weightResult = result.toResultState()) }
        }
    }

    fun fetchRecommendFoods(yearMonth: String, week: Int? = null) {
        updateState { it.copy(recommendFoods = ResultState.Loading) }

        viewModelScope.launch {
            val result = getRecommendFoodUseCase(yearMonth, week)
            updateState { it.copy(recommendFoods = result.toResultState()) }
        }
    }

}