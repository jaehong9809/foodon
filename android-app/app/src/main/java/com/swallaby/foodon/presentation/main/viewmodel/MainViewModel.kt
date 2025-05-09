package com.swallaby.foodon.presentation.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.result.toResultState
import com.swallaby.foodon.domain.calendar.model.CalendarType
import com.swallaby.foodon.domain.calendar.model.Effect
import com.swallaby.foodon.domain.calendar.model.RecommendFood
import com.swallaby.foodon.domain.calendar.usecase.GetCalendarUseCase
import com.swallaby.foodon.domain.calendar.usecase.GetRecommendFoodUseCase
import com.swallaby.foodon.domain.main.usecase.GetMealRecordUseCase
import com.swallaby.foodon.domain.main.usecase.GetNutrientIntakeUseCase
import com.swallaby.foodon.domain.main.usecase.GetNutrientManageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMealRecordUseCase: GetMealRecordUseCase,
    private val getNutrientIntakeUseCase: GetNutrientIntakeUseCase,
    private val getNutrientManageUseCase: GetNutrientManageUseCase,
    private val getRecommendFoodUseCase: GetRecommendFoodUseCase,
    private val getCalendarUseCase: GetCalendarUseCase,
) : BaseViewModel<MainUiState>(MainUiState()) {

    fun updateState(block: (MainUiState) -> MainUiState) {
        _uiState.update(block)
    }

    fun selectDate(date: LocalDate) {
        updateState { it.copy(selectedDate = date) }
    }

    fun fetchCalendarData(date: String) {
        updateState { it.copy(calendarResult = ResultState.Loading) }

        viewModelScope.launch {
            val result = getCalendarUseCase(CalendarType.MEAL, date)
            updateState { it.copy(calendarResult = result.toResultState()) }
        }
    }

    fun fetchRecordData(date: String) {
        updateState { it.copy(recordResult = ResultState.Loading) }

        viewModelScope.launch {
            val result = getMealRecordUseCase(date)
            updateState { it.copy(recordResult = result.toResultState()) }
        }
    }

    fun fetchIntakeData(date: String) {
        updateState { it.copy(intakeResult = ResultState.Loading) }

        viewModelScope.launch {
            val result = getNutrientIntakeUseCase(date)
            updateState { it.copy(intakeResult = result.toResultState()) }
        }
    }

    fun fetchManageData(date: String) {
        updateState { it.copy(manageResult = ResultState.Loading) }

        viewModelScope.launch {
            val result = getNutrientManageUseCase(date)
            updateState { it.copy(manageResult = result.toResultState()) }
        }
    }

    fun fetchRecommendFoods(yearMonth: String, week: Int? = null) {
        updateState { it.copy(recommendMealResult = ResultState.Loading) }

        viewModelScope.launch {
//            val result = getRecommendFoodUseCase(yearMonth, week)
//            updateState { it.copy(recommendMealResult = result.toResultState()) }

            val fakeData = createFakeRecommendFoods()

            updateState {
                it.copy(recommendMealResult = ResultState.Success(fakeData))
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
            )
        )
    }

}