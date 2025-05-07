package com.swallaby.foodon.presentation.mealdetail.viewmodel

import android.util.Log
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.food.model.MealInfo
import com.swallaby.foodon.domain.food.model.MealItem
import com.swallaby.foodon.domain.food.model.MealType
import com.swallaby.foodon.domain.food.model.NutrientInfo
import com.swallaby.foodon.domain.food.model.Position
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MealEditViewModel @Inject constructor() : BaseViewModel<MealEditUiState>(MealEditUiState()) {
    private var isInitialized = false

    fun initMeal(mealInfo: MealInfo) {
        Log.d(TAG, "Initializing MealEditViewModel")
        if (!isInitialized) {
            _uiState.update {
                it.copy(mealEditState = ResultState.Success(mealInfo))
            }
            isInitialized = true
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

    private fun calculateTotalCarbs(items: List<MealItem>): Int {
        return items.sumOf { it.nutrientInfo.carbs }
    }

    private fun calculateTotalFat(items: List<MealItem>): Int {
        return items.sumOf { it.nutrientInfo.fat }
    }

    private fun calculateTotalKcal(items: List<MealItem>): Int {
        return items.sumOf { it.nutrientInfo.kcal }
    }

    private fun calculateTotalProtein(items: List<MealItem>): Int {
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
    totalCarbs = 45,
    totalFat = 15,
    totalProtein = 20,
    totalKcal = 390,
    mealItems = listOf(
        MealItem(
            type = "PUBLIC",
            foodId = 1001,
            foodName = "계란 프라이",
            unit = "개",
            quantity = 2,
            nutrientInfo = NutrientInfo(
                kcal = 140,
                protein = 12,
                fat = 10,
                carbs = 2,
                sugar = 0,
                fiber = 0,
                sodium = 140,
                cholesterol = 370,
                potassium = 120,
                saturatedFat = 3,
                unsaturatedFat = 7,
                transFat = 0,
                fattyAcid = 5,
                alcohol = 0
            ),
            position = listOf(
                Position(
                    height = 120.0, width = 130.0, x = 50, y = 100
                )
            )
        ), MealItem(
            type = "PUBLIC",
            foodId = 1002,
            foodName = "토스트",
            unit = "조각",
            quantity = 2,
            nutrientInfo = NutrientInfo(
                kcal = 180,
                protein = 6,
                fat = 3,
                carbs = 32,
                sugar = 3,
                fiber = 2,
                sodium = 200,
                cholesterol = 0,
                potassium = 70,
                saturatedFat = 1,
                unsaturatedFat = 2,
                transFat = 0,
                fattyAcid = 1,
                alcohol = 0
            ),
            position = listOf(
                Position(
                    height = 80.0, width = 150.0, x = 200, y = 120
                )
            )
        ), MealItem(
            type = "PUBLIC",
            foodId = 1003,
            foodName = "오렌지 주스",
            unit = "ml",
            quantity = 250,
            nutrientInfo = NutrientInfo(
                kcal = 70,
                protein = 2,
                fat = 2,
                carbs = 11,
                sugar = 9,
                fiber = 1,
                sodium = 5,
                cholesterol = 0,
                potassium = 450,
                saturatedFat = 0,
                unsaturatedFat = 2,
                transFat = 0,
                fattyAcid = 0,
                alcohol = 0
            ),
            position = listOf(
                Position(
                    height = 150.0, width = 70.0, x = 400, y = 80
                )
            )
        )
    )
)