package com.swallaby.foodon.presentation.calendar.viewmodel

import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.result.toResultState
import com.swallaby.foodon.core.util.DateUtil.getWeekOfMonth
import com.swallaby.foodon.core.util.FetchTracker
import com.swallaby.foodon.domain.calendar.model.CalendarType
import com.swallaby.foodon.domain.calendar.usecase.GetCalendarUseCase
import com.swallaby.foodon.domain.calendar.usecase.GetRecommendFoodUseCase
import com.swallaby.foodon.domain.calendar.usecase.GetUserWeightUseCase
import com.swallaby.foodon.presentation.sharedstate.CalendarSharedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getCalendarUseCase: GetCalendarUseCase,
    private val getUserWeightUseCase: GetUserWeightUseCase,
    private val getRecommendFoodUseCase: GetRecommendFoodUseCase,
    val calendarSharedState: CalendarSharedState,
) : BaseViewModel<CalendarUiState>(CalendarUiState()) {

    private val yearMonthTracker = FetchTracker(calendarSharedState.currentYearMonth.value)
    private val recommendationTracker = FetchTracker(
        calendarSharedState.currentYearMonth.value to getWeekOfMonth(calendarSharedState.currentWeekStart.value)
    )

    fun updateState(block: (CalendarUiState) -> CalendarUiState) {
        _uiState.update(block)
    }

    fun selectTab(index: Int) {
        updateState { it.copy(selectedTabIndex = index) }
    }

    fun updateInitialLoaded(state: Boolean) {
        updateState { it.copy(isInitialLoaded = state) }
    }

    fun updateCalendarData(
        calendarType: CalendarType,
        isTabChanged: Boolean = false
    ) {
        val today = LocalDate.now()
        val currentYearMonth = calendarSharedState.currentYearMonth.value
        val isSameMonth = YearMonth.from(today) == currentYearMonth

        val newSelectedDate = when {
            !uiState.value.isInitialLoaded || isTabChanged -> calendarSharedState.selectedDate.value
            isSameMonth -> today
            else -> currentYearMonth.atDay(1)
        }

        calendarSharedState.updateDate(newSelectedDate)

        updateState {
            it.copy(
                isInitialLoaded = true
            )
        }

        if (calendarType == CalendarType.RECOMMENDATION) {
            val week = getWeekOfMonth(newSelectedDate)
            updateRecommendation(currentYearMonth, week)
        }

        if (isTabChanged && calendarType == CalendarType.WEIGHT) {
            fetchUserWeight()
        }

        fetchCalendarData(calendarType, currentYearMonth)
    }

    fun fetchCalendarData(type: CalendarType, yearMonth: YearMonth) {
        if (yearMonthTracker.shouldFetch(yearMonth)) {
            viewModelScope.launch {
                calendarSharedState.updateCalendarResult(ResultState.Loading)
                val result = getCalendarUseCase(type, yearMonth)
                calendarSharedState.updateCalendarResult(result.toResultState())
            }
        }
    }

    fun updateRecommendation(yearMonth: YearMonth, week: Int) {
        selectWeek(week - 1)

        fetchRecommendFoods(
            yearMonth = yearMonth,
            week = week
        )
    }

    private fun fetchRecommendFoods(yearMonth: YearMonth, week: Int) {
        if (recommendationTracker.shouldFetch(yearMonth to week)) {
            viewModelScope.launch {
                calendarSharedState.updateRecommendFoods(ResultState.Loading)
                val result = getRecommendFoodUseCase(yearMonth, week)
                calendarSharedState.updateRecommendFoods(result.toResultState())
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