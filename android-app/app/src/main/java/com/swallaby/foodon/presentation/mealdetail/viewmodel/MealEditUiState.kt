package com.swallaby.foodon.presentation.mealdetail.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.food.model.MealItem

data class MealEditUiState(
    val mealEditState: ResultState<MealItem> = ResultState.Success(
        MealItem()
    ),
) : UiState