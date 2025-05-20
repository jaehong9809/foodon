package com.swallaby.foodon.presentation.foodregister.viewmodel

import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.result.toResultState
import com.swallaby.foodon.domain.food.model.FoodInfo
import com.swallaby.foodon.domain.food.model.toRequest
import com.swallaby.foodon.domain.food.usecase.RegisterCustomFoodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodRegisterViewModel @Inject constructor(
    private val registerCustomFoodUseCase: RegisterCustomFoodUseCase,
) : BaseViewModel<FoodRegisterUiState>(FoodRegisterUiState()) {
    private val _events = MutableSharedFlow<FoodRegisterEvent>()
    val events = _events.asSharedFlow()

    fun registerFood(foodInfo: FoodInfo) {
        viewModelScope.launch {
            when (val result = registerCustomFoodUseCase(foodInfo.toRequest()).toResultState()) {
                is ResultState.Success -> {
                    _events.emit(FoodRegisterEvent.NavigateToSearch(result.data))
                }

                is ResultState.Error -> {
                    _events.emit(FoodRegisterEvent.ShowErrorMessage(result.messageRes))

                }

                else -> {}
            }

        }
    }


}