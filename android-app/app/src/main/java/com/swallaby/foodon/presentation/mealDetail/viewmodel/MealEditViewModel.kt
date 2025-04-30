package com.swallaby.foodon.presentation.mealDetail.viewmodel

import com.swallaby.foodon.core.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class MealEditViewModel @Inject constructor() : BaseViewModel<MealEditUiState>(MealEditUiState()) {
    private val _totalKcal = MutableStateFlow(0)
    val totalKcal = _totalKcal.asStateFlow()

    private val _totalCarbs = MutableStateFlow(0)
    val totalCarbs = _totalCarbs.asStateFlow()

    private val _totalProtein = MutableStateFlow(0)
    val totalProtein = _totalProtein.asStateFlow()

    private val _totalFat = MutableStateFlow(0)
    val totalFat = _totalFat.asStateFlow()


}