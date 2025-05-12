package com.swallaby.foodon.presentation.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.result.toResultState
import com.swallaby.foodon.domain.main.usecase.GetMealRecordUseCase
import com.swallaby.foodon.domain.main.usecase.GetNutrientIntakeUseCase
import com.swallaby.foodon.domain.main.usecase.GetNutrientManageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMealRecordUseCase: GetMealRecordUseCase,
    private val getNutrientIntakeUseCase: GetNutrientIntakeUseCase,
    private val getNutrientManageUseCase: GetNutrientManageUseCase,
) : BaseViewModel<MainUiState>(MainUiState()) {

    fun updateState(block: (MainUiState) -> MainUiState) {
        _uiState.update(block)
    }

    fun fetchRecordData(date: String) {
        updateState { it.copy(recordResult = ResultState.Loading) }

        viewModelScope.launch {
            val result = getMealRecordUseCase(date)
            updateState { it.copy(recordResult = result.toResultState()) }
        }
    }

    fun fetchIntakeData(date: String) {
        updateState { it.copy(intakeResult = ResultState.Loading) }

        viewModelScope.launch {
            val result = getNutrientIntakeUseCase(date)
            updateState { it.copy(intakeResult = result.toResultState()) }
        }
    }

    fun fetchManageData(date: String) {
        updateState { it.copy(manageResult = ResultState.Loading) }

        viewModelScope.launch {
            val result = getNutrientManageUseCase(date)
            updateState { it.copy(manageResult = result.toResultState()) }
        }
    }

}