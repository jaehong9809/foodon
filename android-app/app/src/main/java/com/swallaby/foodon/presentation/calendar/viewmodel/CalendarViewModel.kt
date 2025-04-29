package com.swallaby.foodon.presentation.calendar.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.calendar.model.CalendarItem
import com.swallaby.foodon.domain.calendar.model.CalendarMeal
import com.swallaby.foodon.domain.calendar.model.CalendarRecommendation
import com.swallaby.foodon.domain.calendar.model.CalendarType
import com.swallaby.foodon.domain.calendar.model.CalendarWeight
import com.swallaby.foodon.domain.calendar.model.Effect
import com.swallaby.foodon.domain.calendar.model.RecommendFood
import com.swallaby.foodon.domain.calendar.model.UserWeight
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

    fun selectWeek(index: Int) {
        updateState { it.copy(selectedWeekIndex = index) }
    }

    fun fetchCalendarData(type: CalendarType, date: String) {
        updateState { it.copy(calendarState = ResultState.Loading) }

        Log.d("Calendar ViewModel", date)

        viewModelScope.launch {
//            val result = getCalendarUseCase(type, date)
//            updateState { it.copy(calendarState = result.toResultState()) }

            // TODO: 서버 연동 시 삭제
            val fakeData: List<CalendarItem> = createFakeData(type)

            updateState {
                it.copy(calendarState = ResultState.Success(fakeData))
            }
        }
    }

    fun fetchUserWeight() {
        updateState { it.copy(weightState = ResultState.Loading) }

        viewModelScope.launch {
//            val result = getUserWeightUseCase()
//            updateState { it.copy(weightState = result.toResultState()) }

            val fakeData = UserWeight(1, 2)
            updateState { it.copy(weightState = ResultState.Success(fakeData)) }
        }
    }

    fun fetchRecommendFoods(yearMonth: String, week: Int? = null) {
        updateState { it.copy(recommendFoods = ResultState.Loading) }

        Log.d("Calendar ViewModel", "$yearMonth $week")

        viewModelScope.launch {
//            val result = getRecommendFoodUseCase(yearMonth, week)
//            updateState { it.copy(recommendFoods = result.toResultState()) }

            val fakeData = createFakeRecommendFoods()

            updateState {
                it.copy(recommendFoods = ResultState.Success(fakeData))
            }
        }
    }

    private fun createFakeData(type: CalendarType): List<CalendarItem> {
        return when (type) {
            CalendarType.MEAL -> listOf(
                CalendarItem.Meal(
                    data = CalendarMeal(
                        calendarType = CalendarType.MEAL,
                        intakeLogId = 1L,
                        date = "2025-04-01",
                        intakeKcal = 1800,
                        goalKcal = 2000
                    )
                ),
                CalendarItem.Meal(
                    data = CalendarMeal(
                        calendarType = CalendarType.MEAL,
                        intakeLogId = 2L,
                        date = "2025-04-05",
                        intakeKcal = 1950,
                        goalKcal = 2000
                    )
                )
            )

            CalendarType.WEIGHT -> listOf(
                CalendarItem.Weight(
                    data = CalendarWeight(
                        calendarType = CalendarType.WEIGHT,
                        weightRecordId = 3L,
                        date = "2025-04-03",
                        weight = 68
                    )
                ),
                CalendarItem.Weight(
                    data = CalendarWeight(
                        calendarType = CalendarType.WEIGHT,
                        weightRecordId = 4L,
                        date = "2025-04-09",
                        weight = 67
                    )
                )
            )

            CalendarType.RECOMMENDATION -> listOf(
                CalendarItem.Recommendation(
                    data = CalendarRecommendation(
                        calendarType = CalendarType.RECOMMENDATION,
                        mealId = 5L,
                        date = "2025-04-07",
                        thumbnailImage = "https://img.freepik.com/free-photo/top-view-table-full-food_23-2149209253.jpg?semt=ais_hybrid&w=740"
                    )
                ),
                CalendarItem.Recommendation(
                    data = CalendarRecommendation(
                        calendarType = CalendarType.RECOMMENDATION,
                        mealId = 6L,
                        date = "2025-04-10",
                        thumbnailImage = "https://img.freepik.com/free-photo/top-view-table-full-food_23-2149209253.jpg?semt=ais_hybrid&w=740"
                    )
                )
            )
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