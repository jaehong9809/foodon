package com.swallaby.foodon.presentation.foodImage.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.food.model.MealInfo

data class FoodImageUiState(
    val foodImageState: ResultState<MealInfo> = ResultState.Loading,
) : UiState
