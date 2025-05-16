package com.swallaby.foodon.presentation.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.result.toResultState
import com.swallaby.foodon.core.util.FetchTracker
import com.swallaby.foodon.domain.calendar.model.CalendarType
import com.swallaby.foodon.domain.calendar.usecase.GetCalendarUseCase
import com.swallaby.foodon.domain.calendar.usecase.GetRecommendFoodUseCase
import com.swallaby.foodon.domain.main.usecase.GetGoalManageUseCase
import com.swallaby.foodon.domain.main.usecase.GetMealRecordUseCase
import com.swallaby.foodon.domain.main.usecase.GetNutrientIntakeUseCase
import com.swallaby.foodon.domain.main.usecase.GetNutrientManageUseCase
import com.swallaby.foodon.presentation.sharedstate.AppSharedState
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
    private val getGoalManageUseCase: GetGoalManageUseCase,
    private val appSharedState: AppSharedState,
    val calendarSharedState: CalendarSharedState,
) : BaseViewModel<MainUiState>(MainUiState()) {

    private val dateTracker = FetchTracker<LocalDate>()
    private val yearMonthTracker = FetchTracker<YearMonth>()
    private val recommendationTracker = FetchTracker<Pair<YearMonth, Int>>()

    fun updateState(block: (MainUiState) -> MainUiState) {
        _uiState.update(block)
    }

    fun updateDailyData(date: LocalDate) {
        viewModelScope.launch {
            appSharedState.withLoginAndFetch(date, dateTracker) {
                fetchRecordData(date)
                fetchIntakeData(date)
                fetchNutrientManageData(date)
                fetchGoalManageData()
            }
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

    private fun fetchNutrientManageData(date: LocalDate) {
        updateState { it.copy(nutrientManageResult = ResultState.Loading) }

        viewModelScope.launch {
            val result = getNutrientManageUseCase(date)
            updateState { it.copy(nutrientManageResult = result.toResultState()) }
        }
    }

    private fun fetchGoalManageData() {
        updateState { it.copy(goalManageResult = ResultState.Loading) }

        viewModelScope.launch {
            val result = getGoalManageUseCase()
            updateState { it.copy(goalManageResult = result.toResultState()) }
        }
    }

    fun fetchCalendarData(yearMonth: YearMonth) {
        viewModelScope.launch {
            appSharedState.withLoginAndFetch(yearMonth, yearMonthTracker) {
                updateState { it.copy(mealCalendarResult = ResultState.Loading) }
                val result = getCalendarUseCase(CalendarType.MEAL, yearMonth)
                updateState { it.copy(mealCalendarResult = result.toResultState()) }

                if (calendarSharedState.calendarType.value == CalendarType.MEAL) {
                    calendarSharedState.updateCalendarResult(result = result.toResultState())
                }
            }
        }
    }

    fun fetchRecommendation(yearMonth: YearMonth, week: Int) {
        viewModelScope.launch {
            appSharedState.withLoginAndFetch(yearMonth to week, recommendationTracker) {
                calendarSharedState.updateRecommendFoods(ResultState.Loading)
                val result = getRecommendFoodUseCase(yearMonth, week)
                calendarSharedState.updateRecommendFoods(result.toResultState())
            }
        }
    }

}