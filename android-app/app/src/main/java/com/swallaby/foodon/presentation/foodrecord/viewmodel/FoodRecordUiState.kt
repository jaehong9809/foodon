package com.swallaby.foodon.presentation.foodrecord.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.food.model.MealInfo

data class FoodRecordUiState(
    val foodRecordState: ResultState<MealInfo> = ResultState.Loading,
) : UiState
