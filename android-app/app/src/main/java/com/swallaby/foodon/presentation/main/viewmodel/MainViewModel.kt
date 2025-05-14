package com.swallaby.foodon.presentation.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.result.toResultState
import com.swallaby.foodon.core.util.FetchTracker
import com.swallaby.foodon.domain.calendar.model.CalendarType
import com.swallaby.foodon.domain.calendar.usecase.GetCalendarUseCase
import com.swallaby.foodon.domain.calendar.usecase.GetRecommendFoodUseCase
import com.swallaby.foodon.domain.main.usecase.GetMealRecordUseCase
import com.swallaby.foodon.domain.main.usecase.GetNutrientIntakeUseCase
import com.swallaby.foodon.domain.main.usecase.GetNutrientManageUseCase
import com.swallaby.foodon.presentation.sharedstate.CalendarSharedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMealRecordUseCase: GetMealRecordUseCase,
    private val getNutrientIntakeUseCase: GetNutrientIntakeUseCase,
    private val getNutrientManageUseCase: GetNutrientManageUseCase,
    private val getCalendarUseCase: GetCalendarUseCase,
    private val getRecommendFoodUseCase: GetRecommendFoodUseCase,
    val calendarSharedState: CalendarSharedState,
) : BaseViewModel<MainUiState>(MainUiState()) {

    private val dateTracker = FetchTracker<LocalDate>()
    private val yearMonthTracker = FetchTracker<YearMonth>()
    private val recommendationTracker = FetchTracker<Pair<YearMonth, Int>>()

    fun updateState(block: (MainUiState) -> MainUiState) {
        _uiState.update(block)
    }

    fun updateDailyData(date: LocalDate) {
        if (dateTracker.shouldFetch(date)) {
            fetchRecordData(date)
            fetchIntakeData(date)
            fetchManageData(date)
        }
    }

    private fun fetchRecordData(date: LocalDate) {
        updateState { it.copy(recordResult = ResultState.Loading) }

        viewModelScope.launch {
            val result = getMealRecordUseCase(date)
            updateState { it.copy(recordResult = result.toResultState()) }
        }
    }

    private fun fetchIntakeData(date: LocalDate) {
        updateState { it.copy(intakeResult = ResultState.Loading) }

        viewModelScope.launch {
            val result = getNutrientIntakeUseCase(date)
            updateState { it.copy(intakeResult = result.toResultState()) }
        }
    }

    private fun fetchManageData(date: LocalDate) {
        updateState { it.copy(manageResult = ResultState.Loading) }

        viewModelScope.launch {
            val result = getNutrientManageUseCase(date)
            updateState { it.copy(manageResult = result.toResultState()) }
        }
    }

    fun fetchCalendarData(ym: YearMonth) {
        if (yearMonthTracker.shouldFetch(ym)) {
            viewModelScope.launch {
                calendarSharedState.updateCalendarResult(ResultState.Loading)
                val result = getCalendarUseCase(CalendarType.MEAL, ym)
                calendarSharedState.updateCalendarResult(result.toResultState())
            }
        }
    }

    fun fetchRecommendation(ym: YearMonth, week: Int) {
        if (recommendationTracker.shouldFetch(ym to week)) {
            viewModelScope.launch {
                calendarSharedState.updateRecommendFoods(ResultState.Loading)
                val result = getRecommendFoodUseCase(ym, week)
                calendarSharedState.updateRecommendFoods(result.toResultState())
            }
        }
    }

}