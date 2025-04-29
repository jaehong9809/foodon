package com.swallaby.foodon.presentation.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.result.toResultState
import com.swallaby.foodon.domain.main.usecase.GetMealRecordUseCase
import com.swallaby.foodon.domain.main.usecase.GetNutrientIntakeUseCase
import com.swallaby.foodon.domain.main.usecase.GetNutrientManageUseCase
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getMealRecordUseCase: GetMealRecordUseCase,
    private val getNutrientIntakeUseCase: GetNutrientIntakeUseCase,
    private val getNutrientManageUseCase: GetNutrientManageUseCase,
) : BaseViewModel<MainUiState>(MainUiState()) {
    
    fun updateState(block: (MainUiState) -> MainUiState) {
        _uiState.update(block)
    }

    fun fetchRecordData(day: String) {
        updateState { it.copy(recordState = ResultState.Loading) }

        viewModelScope.launch {
            val result = getMealRecordUseCase(day)
            updateState { it.copy(recordState = result.toResultState()) }
        }
    }

    fun fetchIntakeData(day: String) {
        updateState { it.copy(intakeState = ResultState.Loading) }

        viewModelScope.launch {
            val result = getNutrientIntakeUseCase(day)
            updateState { it.copy(intakeState = result.toResultState()) }
        }
    }

    fun fetchManageData(day: String) {
        updateState { it.copy(manageState = ResultState.Loading) }

        viewModelScope.launch {
            val result = getNutrientManageUseCase(day)
            updateState { it.copy(manageState = result.toResultState()) }
        }
    }

}