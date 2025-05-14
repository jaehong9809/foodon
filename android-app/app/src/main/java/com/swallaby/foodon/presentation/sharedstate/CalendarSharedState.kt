package com.swallaby.foodon.presentation.sharedstate

import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.calendar.model.CalendarItem
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

    val today: StateFlow<LocalDate> = MutableStateFlow(LocalDate.now())

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    private val _currentYearMonth = MutableStateFlow(YearMonth.now())
    val currentYearMonth: StateFlow<YearMonth> = _currentYearMonth

    private val _calendarResult = MutableStateFlow<ResultState<List<CalendarItem>>>(ResultState.Loading)
    val calendarResult: StateFlow<ResultState<List<CalendarItem>>> = _calendarResult

    private val _recommendFoods = MutableStateFlow<ResultState<List<RecommendFood>>>(ResultState.Loading)
    val recommendFoods: StateFlow<ResultState<List<RecommendFood>>> = _recommendFoods

    val currentWeekStart: StateFlow<LocalDate> = _selectedDate.map {
        it.with(weekFields.dayOfWeek(), 1)
    }.stateIn(
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
        started = SharingStarted.Eagerly,
        initialValue = LocalDate.now().with(weekFields.dayOfWeek(), 1)
    )

    fun updateDate(date: LocalDate) {
        _selectedDate.value = date
        _currentYearMonth.value = YearMonth.from(date)
    }

    fun updateMonth(month: YearMonth) {
        _currentYearMonth.value = month
    }

    fun goToWeek(delta: Int) {
        val todayValue = today.value
        val targetWeekStart = selectedDate.value.plusWeeks(delta.toLong()).with(weekFields.dayOfWeek(), 1)
        val todayWeekStart = todayValue.with(weekFields.dayOfWeek(), 1)

        val newDate = if (targetWeekStart == todayWeekStart) todayValue else targetWeekStart

        _selectedDate.value = newDate
        _currentYearMonth.value = YearMonth.from(newDate)
    }

    fun resetToTodayWeek() {
        _selectedDate.value = today.value
        _currentYearMonth.value = YearMonth.from(today.value)
    }

    fun updateCalendarResult(result: ResultState<List<CalendarItem>>) {
        _calendarResult.value = result
    }

    fun updateRecommendFoods(result: ResultState<List<RecommendFood>>) {
        _recommendFoods.value = result
    }
}
