package com.swallaby.foodon.presentation.mealdetail.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.food.model.MealInfo

sealed class MealEditEvent {
    object NavigateToMain : MealEditEvent()
    data class ShowErrorMessage(val errorMessageRes: Int) : MealEditEvent()
}

data class MealEditUiState(
    val mealEditState: ResultState<MealInfo> = ResultState.Success(
        MealInfo()
    ),
) : UiState