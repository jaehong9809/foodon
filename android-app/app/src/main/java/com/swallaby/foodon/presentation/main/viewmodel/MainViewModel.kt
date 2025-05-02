package com.swallaby.foodon.presentation.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.calendar.model.Effect
import com.swallaby.foodon.domain.calendar.model.RecommendFood
import com.swallaby.foodon.domain.calendar.usecase.GetRecommendFoodUseCase
import com.swallaby.foodon.domain.main.model.MealRecord
import com.swallaby.foodon.domain.main.model.MealTimeType
import com.swallaby.foodon.domain.main.model.NutrientIntake
import com.swallaby.foodon.domain.main.model.NutrientManage
import com.swallaby.foodon.domain.main.model.NutrientManageType
import com.swallaby.foodon.domain.main.model.NutrientStatus
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
    private val getRecommendFoodUseCase: GetRecommendFoodUseCase,
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

    fun fetchRecordData(date: String) {
        updateState { it.copy(recordResult = ResultState.Loading) }

        viewModelScope.launch {
//            val result = getMealRecordUseCase(date)
//            updateState { it.copy(recordState = result.toResultState()) }

            val fakeData = createFakeMealRecord()
            updateState { it.copy(recordResult = ResultState.Success(fakeData)) }
        }
    }

    fun fetchIntakeData(date: String) {
        updateState { it.copy(intakeResult = ResultState.Loading) }

        viewModelScope.launch {
//            val result = getNutrientIntakeUseCase(date)
//            updateState { it.copy(intakeResult = result.toResultState()) }

            val fakeData = createFakeIntake()
            updateState { it.copy(intakeResult = ResultState.Success(fakeData)) }
        }
    }

    fun fetchManageData(date: String) {
        updateState { it.copy(manageResult = ResultState.Loading) }

        viewModelScope.launch {
//            val result = getNutrientManageUseCase(date)
//            updateState { it.copy(manageResult = result.toResultState()) }

            val fakeData = createFakeManage()
            updateState { it.copy(manageResult = ResultState.Success(fakeData)) }
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

    private fun createFakeManage(): List<NutrientManage> {
        return listOf(
            NutrientManage(
                nutrientName = "당류",
                manageType = NutrientManageType.ESSENTIAL,
                unit = "g",
                intake = 10,
                minRecommend = 0,
                maxRecommend = 50,
                status = NutrientStatus.DANGER
            ),
            NutrientManage(
                nutrientName = "나트륨",
                manageType = NutrientManageType.LIMITED,
                unit = "mg",
                intake = 4000,
                minRecommend = 0,
                maxRecommend = 5000,
                status = NutrientStatus.LACK
            ),
            NutrientManage(
                nutrientName = "포화지방",
                manageType = NutrientManageType.LIMITED,
                unit = "g",
                intake = 100,
                minRecommend = 0,
                maxRecommend = 20,
                status = NutrientStatus.CAUTION
            ),
            NutrientManage(
                nutrientName = "트랜스지방",
                manageType = NutrientManageType.ESSENTIAL,
                unit = "g",
                intake = 10,
                minRecommend = 0,
                maxRecommend = 2,
                status = NutrientStatus.CAUTION
            ),
            NutrientManage(
                nutrientName = "카페인",
                manageType = NutrientManageType.ESSENTIAL,
                unit = "mg",
                intake = 400,
                minRecommend = 0,
                maxRecommend = 400,
                status = NutrientStatus.NORMAL
            ),
            NutrientManage(
                nutrientName = "알코올",
                manageType = NutrientManageType.LIMITED,
                unit = "g",
                intake = 100,
                minRecommend = 0,
                maxRecommend = 30,
                status = NutrientStatus.CAUTION
            )
        )
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