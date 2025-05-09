package com.swallaby.foodon.presentation.foodedit.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.food.model.MealInfo
import com.swallaby.foodon.presentation.mealdetail.viewmodel.createDummyMealInfo

data class FoodEditUiState(
    val foodEditState: ResultState<MealInfo> = ResultState.Success(
        MealInfo()
    )
) : UiState
