package com.swallaby.foodon.presentation.mealDetail.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.food.model.MealNutrientWithPosition

data class MealEditUiState(
    val mealEditState: ResultState<MealNutrientWithPosition> = ResultState.Success(
        MealNutrientWithPosition()
    ),
) : UiState