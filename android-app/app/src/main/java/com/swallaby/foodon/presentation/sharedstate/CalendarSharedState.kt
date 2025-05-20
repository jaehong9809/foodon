package com.swallaby.foodon.presentation.sharedstate

import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.calendar.model.CalendarItem
import com.swallaby.foodon.domain.calendar.model.CalendarType
import com.swallaby.foodon.domain.calendar.model.RecommendFood
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.temporal.WeekFields
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarSharedState @Inject constructor() {

    private val weekFields = WeekFields.of(DayOfWeek.SUNDAY, 1)

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    private val _currentYearMonth = MutableStateFlow(YearMonth.now())
    val currentYearMonth: StateFlow<YearMonth> = _currentYearMonth

    private val _calendarResult = MutableStateFlow<ResultState<List<CalendarItem>>>(ResultState.Loading)
    val calendarResult: StateFlow<ResultState<List<CalendarItem>>> = _calendarResult

    private val _recommendFoods = MutableStateFlow<ResultState<List<RecommendFood>>>(ResultState.Loading)
    val recommendFoods: StateFlow<ResultState<List<RecommendFood>>> = _recommendFoods

    private val _calendarType = MutableStateFlow<CalendarType>(CalendarType.MEAL)
    val calendarType: StateFlow<CalendarType> = _calendarType

    val currentWeekStart: StateFlow<LocalDate> = _selectedDate.map {
        it.with(weekFields.dayOfWeek(), 1)
    }.stateIn(
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
        started = SharingStarted.Eagerly,
        initialValue = LocalDate.now().with(weekFields.dayOfWeek(), 1)
    )

    private val _refreshGoal = MutableStateFlow(true)

    val refreshGoal: StateFlow<Boolean> = _refreshGoal

    fun refreshForGoal() {
        _refreshGoal.value = true
    }

    fun clearGoal() {
        _refreshGoal.value = false
    }

    fun updateDate(date: LocalDate) {
        _selectedDate.value = date
        _currentYearMonth.value = YearMonth.from(date)
    }

    fun updateMonth(month: YearMonth) {
        _currentYearMonth.value = month
    }

    fun goToWeek(delta: Int) {
        val today = LocalDate.now()
        val targetWeekStart = selectedDate.value.plusWeeks(delta.toLong()).with(weekFields.dayOfWeek(), 1)
        val todayWeekStart = today.with(weekFields.dayOfWeek(), 1)

        val newDate = if (targetWeekStart == todayWeekStart) today else targetWeekStart

        _selectedDate.value = newDate
        _currentYearMonth.value = YearMonth.from(newDate)
    }

    fun resetToTodayWeek() {
        val today = LocalDate.now()

        _selectedDate.value = today
        _currentYearMonth.value = YearMonth.from(today)
    }

    fun updateCalendarResult(
        type: CalendarType = CalendarType.MEAL,
        result: ResultState<List<CalendarItem>>
    ) {
        _calendarResult.value = result
        _calendarType.value = type
    }

    fun updateRecommendFoods(result: ResultState<List<RecommendFood>>) {
        _recommendFoods.value = result
    }
}
