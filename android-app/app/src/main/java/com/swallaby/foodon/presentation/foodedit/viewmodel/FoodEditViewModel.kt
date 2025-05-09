package com.swallaby.foodon.presentation.foodedit.viewmodel

import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.food.model.MealInfo
import com.swallaby.foodon.domain.food.model.NutrientInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FoodEditViewModel @Inject constructor(
) : BaseViewModel<FoodEditUiState>(FoodEditUiState()) {
    private var isInitialized = false

    fun initFood(mealInfo: MealInfo) {
        if (!isInitialized) {
            _uiState.update {
                it.copy(
                    foodEditState = ResultState.Success(mealInfo),
                    selectedFoodId = mealInfo.mealItems.firstOrNull()?.foodId ?: 0
                )
            }
            isInitialized = true
        }
    }

    fun updateFoodNutrients(foodId: Long, updatedNutrientInfo: NutrientInfo) {
        val currentUiState = _uiState.value
        if (currentUiState.foodEditState is ResultState.Success) {
            val mealInfo = currentUiState.foodEditState.data

            val updatedItems = mealInfo.mealItems.map { item ->
                if (item.foodId == foodId) {
                    val newItem = item.copy(nutrientInfo = updatedNutrientInfo)
                    newItem
                } else {
                    item
                }
            }

            val updatedMealInfo = mealInfo.copy(mealItems = updatedItems)

            _uiState.update { currentState ->
                currentState.copy(
                    foodEditState = ResultState.Success(updatedMealInfo)
                )
            }

        }
    }

    fun selectFood(foodId: Long) {
        _uiState.update {
            it.copy(selectedFoodId = foodId)
        }
    }

}