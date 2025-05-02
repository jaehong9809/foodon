package com.swallaby.foodon.presentation.mealdetail.viewmodel

import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.domain.food.model.MealType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class MealEditViewModel @Inject constructor() : BaseViewModel<MealEditUiState>(MealEditUiState()) {
    init {

    }

    fun updateMealType(mealType: MealType) {
        _uiState.update { it.copy(mealType = mealType) }
    }

    fun updateMealTime(mealTime: String) {
        _uiState.update { it.copy(mealTime = mealTime) }
    }

}