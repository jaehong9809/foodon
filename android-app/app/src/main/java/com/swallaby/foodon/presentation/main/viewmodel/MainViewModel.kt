package com.swallaby.foodon.presentation.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.result.toResultState
import com.swallaby.foodon.domain.main.model.MealRecord
import com.swallaby.foodon.domain.main.model.MealTimeType
import com.swallaby.foodon.domain.main.model.NutrientIntake
import com.swallaby.foodon.domain.main.usecase.GetMealRecordUseCase
import com.swallaby.foodon.domain.main.usecase.GetNutrientIntakeUseCase
import com.swallaby.foodon.domain.main.usecase.GetNutrientManageUseCase
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
) : BaseViewModel<MainUiState>(MainUiState()) {

    fun updateState(block: (MainUiState) -> MainUiState) {
        _uiState.update(block)
    }

    fun updateYearMonth(yearMonth: YearMonth) {
        updateState { it.copy(currentYearMonth = yearMonth) }
    }

    fun selectDate(date: LocalDate) {
        updateState { it.copy(selectedDate = date) }
    }

    fun fetchRecordData(day: String) {
        updateState { it.copy(recordResult = ResultState.Loading) }

        viewModelScope.launch {
//            val result = getMealRecordUseCase(day)
//            updateState { it.copy(recordState = result.toResultState()) }

            val fakeData = createFakeMealRecord()
            updateState { it.copy(recordResult = ResultState.Success(fakeData)) }
        }
    }

    fun fetchIntakeData(day: String) {
        updateState { it.copy(intakeResult = ResultState.Loading) }

        viewModelScope.launch {
//            val result = getNutrientIntakeUseCase(day)
//            updateState { it.copy(intakeResult = result.toResultState()) }

            val fakeData = createFakeIntake()
            updateState { it.copy(intakeResult = ResultState.Success(fakeData)) }
        }
    }

    fun fetchManageData(day: String) {
        updateState { it.copy(manageResult = ResultState.Loading) }

        viewModelScope.launch {
            val result = getNutrientManageUseCase(day)
            updateState { it.copy(manageResult = result.toResultState()) }
        }
    }

    private fun createFakeMealRecord(): List<MealRecord> {
        return listOf(
            MealRecord(
                mealId = 1,
                mealTimeType = MealTimeType.BREAKFAST,
                mealTime = "08:00",
                mealImageUrl = "https://img.freepik.com/free-photo/top-view-table-full-food_23-2149209253.jpg?semt=ais_hybrid&w=740",
                mealItems = listOf(
                    "쌀밥",
                    "된장찌개",
                    "김치",
                    "쌀밥",
                    "된장찌개",
                    "김치",
                    "쌀밥",
                    "된장찌개",
                    "김치",
                    "쌀밥",
                    "된장찌개",
                    "김치",
                ),
                totalKcal = 1000,
                totalCarbs = 10,
                totalProtein = 5,
                totalFat = 5
            ),
            MealRecord(
                mealId = 1,
                mealTimeType = MealTimeType.LUNCH,
                mealTime = "08:00",
                mealImageUrl = "",
                mealItems = listOf(
                    "쌀밥"
                ),
                totalKcal = 1000,
                totalCarbs = 10,
                totalProtein = 5,
                totalFat = 5
            )
        )
    }

    private fun createFakeIntake(): NutrientIntake {
        return NutrientIntake(
            intakeKcal = 1000,
            goalKcal = 1600,
            intakeCarbs = 50,
            targetCarbs = 100,
            intakeProtein = 10,
            targetProtein = 100,
            intakeFat = 10,
            targetFat = 100
        )
    }

}