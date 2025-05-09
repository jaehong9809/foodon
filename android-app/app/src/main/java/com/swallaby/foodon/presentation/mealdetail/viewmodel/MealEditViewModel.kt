package com.swallaby.foodon.presentation.mealdetail.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.result.toResultState
import com.swallaby.foodon.domain.food.model.FoodType
import com.swallaby.foodon.domain.food.model.MealInfo
import com.swallaby.foodon.domain.food.model.MealItem
import com.swallaby.foodon.domain.food.model.MealType
import com.swallaby.foodon.domain.food.model.NutrientInfo
import com.swallaby.foodon.domain.food.model.Position
import com.swallaby.foodon.domain.food.model.toRequest
import com.swallaby.foodon.domain.food.usecase.RecordMealUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealEditViewModel @Inject constructor(
    private val recordMealUseCase: RecordMealUseCase,
) : BaseViewModel<MealEditUiState>(MealEditUiState()) {
    private val _events = MutableSharedFlow<MealEditEvent>()
    val events = _events.asSharedFlow()

    init {
        Log.d("MealEditViewModel", "init called")
    }

    fun initMeal(mealInfo: MealInfo) {
        Log.d(TAG, "Initializing MealEditViewModel")
        _uiState.update {
            it.copy(mealEditState = ResultState.Success(mealInfo))
        }
    }

    fun recordMeal() {
        val mealEditUiState = (_uiState.value.mealEditState as ResultState.Success)
        val mealInfo = mealEditUiState.data
        val request = mealInfo.toRequest().copy(
            mealTime = uiState.value.mealTime, mealTimeType = uiState.value.mealType
        )
        viewModelScope.launch {
            when (val result = recordMealUseCase(request).toResultState()) {
                is ResultState.Success -> {
                    _uiState.update { it.copy(mealEditState = ResultState.Success(mealInfo)) }
                    _events.emit(MealEditEvent.NavigateToMain)
                }

                is ResultState.Error -> {
                    val errorMessage = result.messageRes
                    _uiState.update { it.copy(mealEditState = ResultState.Error(messageRes = errorMessage)) }
                }

                else -> {
                    _uiState.update { it.copy(mealEditState = ResultState.Loading) }
                }
            }
        }
    }

    fun updateMealType(mealType: MealType) {
        Log.d(TAG, "Updating meal type: $mealType")
        _uiState.update { it.copy(mealType = mealType) }
    }

    fun updateMealTime(mealTime: String) {
        Log.d(TAG, "Updating meal time: $mealTime")
        _uiState.update { it.copy(mealTime = mealTime) }
    }

    fun updateFood(food: MealItem) {
        Log.d(TAG, "Updating food: ${food.foodId}")
        val currentState = _uiState.value.mealEditState
        if (currentState is ResultState.Success) {
            val mealInfo = currentState.data
            val updatedItems = mealInfo.mealItems.map {
                if (it.foodId == food.foodId) food else it
            }

            _uiState.update {
                it.copy(
                    mealEditState = ResultState.Success(
                        mealInfo.copy(
                            mealItems = updatedItems,
                            totalCarbs = calculateTotalCarbs(updatedItems),
                            totalFat = calculateTotalFat(updatedItems),
                            totalKcal = calculateTotalKcal(updatedItems),
                            totalProtein = calculateTotalProtein(updatedItems)
                        )
                    )
                )
            }
            Log.d(TAG, "Food update complete: ${food.foodId}")
        } else {
            Log.e(TAG, "Cannot update food: Invalid state")
        }
    }

    fun deleteFood(foodId: Long) {
        Log.d(TAG, "Deleting food: $foodId")
        val currentState = _uiState.value.mealEditState
        if (currentState is ResultState.Success) {
            val mealInfo = currentState.data
            val updatedItems = mealInfo.mealItems.filterNot { it.foodId == foodId }

            val updatedMealInfo = mealInfo.copy(
                mealItems = updatedItems,
                totalCarbs = calculateTotalCarbs(updatedItems),
                totalFat = calculateTotalFat(updatedItems),
                totalKcal = calculateTotalKcal(updatedItems),
                totalProtein = calculateTotalProtein(updatedItems)
            )

            _uiState.update { it.copy(mealEditState = ResultState.Success(updatedMealInfo)) }
            Log.d(TAG, "Food deletion complete: $foodId")
        } else {
            Log.e(TAG, "Cannot delete food: Invalid state")
        }
    }

    private fun calculateTotalCarbs(items: List<MealItem>): Double {
        return items.sumOf { it.nutrientInfo.carbs }
    }

    private fun calculateTotalFat(items: List<MealItem>): Double {
        return items.sumOf { it.nutrientInfo.fat }
    }

    private fun calculateTotalKcal(items: List<MealItem>): Int {
        return items.sumOf { it.nutrientInfo.kcal }
    }

    private fun calculateTotalProtein(items: List<MealItem>): Double {
        return items.sumOf { it.nutrientInfo.protein }
    }

    companion object {
        private const val TAG = "MealEditViewModel"
    }
}

fun createDummyMealInfo(): MealInfo = MealInfo(
    imageFileName = "https://example.com/breakfast.jpg",
    mealTime = "2025-05-02 07:30",
    mealTimeType = "BREAKFAST",
    totalCarbs = 45.0,
    totalFat = 15.0,
    totalProtein = 20.0,
    totalKcal = 390,
    mealItems = listOf(
        MealItem(
            type = FoodType.PUBLIC,
            foodId = 1001,
            foodName = "계란 프라이",
            unit = "개",
            quantity = 2,
            nutrientInfo = NutrientInfo(
                kcal = 140,
                protein = 12.0,
                fat = 10.0,
                carbs = 2.0,
                sugar = 0.0,
                fiber = 0.0,
                sodium = 140.0,
                cholesterol = 370.0,
                potassium = 120.0,
                saturatedFat = 3.0,
                unsaturatedFat = 7.0,
                transFat = 0.0,
                fattyAcid = 5.0,
                alcohol = 0.0
            ),
            positions = listOf(
                Position(
                    height = 0.1, width = 0.2, x = 120.0, y = 130.0
                )
            )
        ), MealItem(
            type = FoodType.PUBLIC,
            foodId = 1002,
            foodName = "토스트",
            unit = "조각",
            quantity = 2,
            nutrientInfo = NutrientInfo(
                kcal = 180,
                protein = 6.0,
                fat = 3.0,
                carbs = 32.0,
                sugar = 3.0,
                fiber = 2.0,
                sodium = 200.0,
                cholesterol = 0.0,
                potassium = 70.0,
                saturatedFat = 1.0,
                unsaturatedFat = 2.0,
                transFat = 0.0,
                fattyAcid = 1.0,
                alcohol = .0
            ),
            positions = listOf(
                Position(
                    height = 0.1, width = 0.2, x = 120.0, y = 130.0
                )
            )
        ), MealItem(
            type = FoodType.PUBLIC,
            foodId = 1003,
            foodName = "오렌지 주스",
            unit = "ml",
            quantity = 250,
            nutrientInfo = NutrientInfo(
                kcal = 70,
                protein = 2.0,
                fat = 2.0,
                carbs = 11.0,
                sugar = 9.0,
                fiber = 1.0,
                sodium = 5.0,
                cholesterol = 0.0,
                potassium = 450.0,
                saturatedFat = 0.0,
                unsaturatedFat = 2.0,
                transFat = 0.0,
                fattyAcid = 0.0,
                alcohol = .00
            ),
            positions = listOf(
                Position(
                    height = 0.1, width = 0.2, x = 120.0, y = 130.0
                )
            )
        )
    )
)