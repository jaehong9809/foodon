package com.swallaby.foodon.presentation.mealrecord.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.food.model.MealInfo

sealed class MealRecordEvent {
    data class NavigateToDetail(val mealInfo: MealInfo) : MealRecordEvent()
    data class ShowErrorMessage(val errorMessageRes: Int) : MealRecordEvent()
}


data class MealRecordUiState(
    val mealRecordState: ResultState<MealInfo?> = ResultState.Success(null),
) : UiState
