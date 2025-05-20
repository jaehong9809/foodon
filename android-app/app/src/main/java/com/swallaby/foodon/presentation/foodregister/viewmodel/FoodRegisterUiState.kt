package com.swallaby.foodon.presentation.foodregister.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.food.model.FoodInfo
import com.swallaby.foodon.domain.food.model.FoodInfoWithId

sealed class FoodRegisterEvent {
    data class NavigateToSearch(val food :FoodInfoWithId) : FoodRegisterEvent()
    data class ShowErrorMessage(val errorMessageRes: Int) : FoodRegisterEvent()
}


data class FoodRegisterUiState(
    val foodRegisterState: ResultState<FoodInfo> = ResultState.Success(FoodInfo()),
) : UiState