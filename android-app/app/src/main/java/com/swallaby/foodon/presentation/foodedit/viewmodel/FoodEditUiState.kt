package com.swallaby.foodon.presentation.foodedit.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.food.model.MealInfo
import com.swallaby.foodon.domain.food.model.MealItem

sealed class FoodEditEvent {
    data class SuccessCustomFood(val mealItem: MealItem) : FoodEditEvent()
    data class FailedCustomFood(val messageRes: Int) : FoodEditEvent()
    object SuccessUpdateFood : FoodEditEvent()
}

data class FoodEditUiState(
    val foodEditState: ResultState<MealInfo> = ResultState.Success(
        MealInfo()
    ),

    val selectedFoodId: Long = 0,
) : UiState
