package com.swallaby.foodon.presentation.calendar.viewmodel

import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.result.toResultState
import com.swallaby.foodon.domain.calendar.model.CalendarType
import com.swallaby.foodon.domain.calendar.model.Effect
import com.swallaby.foodon.domain.calendar.model.RecommendFood
import com.swallaby.foodon.domain.calendar.usecase.GetCalendarUseCase
import com.swallaby.foodon.domain.calendar.usecase.GetRecommendFoodUseCase
import com.swallaby.foodon.domain.calendar.usecase.GetUserWeightUseCase
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
) : BaseViewModel<CalendarUiState>(CalendarUiState()) {

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

    private fun selectWeek(index: Int) {
        updateState { it.copy(selectedWeekIndex = index) }
    }

    fun updateRecommendation(currentYearMonth: YearMonth, weekIndex: Int) {
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

    fun fetchUserWeight() {
        updateState { it.copy(weightResult = ResultState.Loading) }

        viewModelScope.launch {
            val result = getUserWeightUseCase()
            updateState { it.copy(weightResult = result.toResultState()) }
        }
    }

    private fun fetchRecommendFoods(yearMonth: String, week: Int? = null) {
        updateState { it.copy(recommendFoods = ResultState.Loading) }

        viewModelScope.launch {
//            val result = getRecommendFoodUseCase(yearMonth, week)
//            updateState { it.copy(recommendFoods = result.toResultState()) }

            val fakeData = createFakeRecommendFoods()

            updateState {
                it.copy(recommendFoods = ResultState.Success(fakeData))
            }
        }
    }

    private fun createFakeRecommendFoods(): List<RecommendFood> {
        return listOf(
            RecommendFood(
                foodRecommendId = 1,
                name = "고구마",
                kcal = 120,
                reason = "에너지원으로 좋아서 추천합니다.",
                effects = listOf(
                    Effect(label = "혈당 조절"),
                    Effect(label = "소화 촉진")
                )
            ),
            RecommendFood(
                foodRecommendId = 2,
                name = "닭가슴살",
                kcal = 165,
                reason = "단백질 섭취를 위해 추천합니다.",
                effects = listOf(
                    Effect(label = "근육 생성"),
                    Effect(label = "포만감 증가")
                )
            ),
            RecommendFood(
                foodRecommendId = 3,
                name = "아몬드",
                kcal = 575,
                reason = "건강한 지방 섭취를 위해 추천합니다.",
                effects = listOf(
                    Effect(label = "심장 건강"),
                    Effect(label = "혈압 조절")
                )
            )
        )
    }

}