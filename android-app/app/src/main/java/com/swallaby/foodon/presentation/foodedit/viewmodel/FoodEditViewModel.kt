package com.swallaby.foodon.presentation.foodedit.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.result.toResultState
import com.swallaby.foodon.data.food.remote.dto.request.CustomFoodRequest
import com.swallaby.foodon.domain.food.model.MealInfo
import com.swallaby.foodon.domain.food.model.MealItem
import com.swallaby.foodon.domain.food.model.NutrientInfo
import com.swallaby.foodon.domain.food.usecase.RegisterCustomFoodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodEditViewModel @Inject constructor(
    private val registerCustomFoodUseCase: RegisterCustomFoodUseCase,
) : BaseViewModel<FoodEditUiState>(FoodEditUiState()) {
    private var isInitialized = false

    private val _events = MutableSharedFlow<FoodEditEvent>()
    val events = _events.asSharedFlow()

    fun initFood(mealInfo: MealInfo, foodId: Long) {
        if (!isInitialized) {
            // 선택된 음식을 가장 앞으로 이동 그 후 이름 순 정렬
            val sortedMealItems = mealInfo.mealItems.sortedWith(compareBy<MealItem> {
                it.foodId != foodId
            }.thenBy {
                it.foodName
            })

            _uiState.update {
                it.copy(
                    foodEditState = ResultState.Success(
                        mealInfo.copy(
                            mealItems = sortedMealItems
                        )
                    ), selectedFoodId = foodId
                )
            }
            isInitialized = true
        }
    }

    fun registerCustomFood(mealItem: MealItem) {
        viewModelScope.launch {
            val request = CustomFoodRequest(
                foodName = mealItem.foodName,
                nutrients = mealItem.nutrientInfo,
                servingSize = mealItem.quantity,
                unit = mealItem.unit
            )

            when (val result = registerCustomFoodUseCase(
                request = request
            ).toResultState()) {
                is ResultState.Success -> {
                    Log.d("FoodEditViewModel", "Success custom food")
                    _events.emit(FoodEditEvent.SuccessCustomFood(mealItem))
                }

                is ResultState.Error -> {
                    Log.d("FoodEditViewModel", "Error registerCustomFood: ${result.messageRes}")
                    _events.emit(FoodEditEvent.FailedCustomFood(result.messageRes))
                }

                else -> {

                }

            }
        }

    }

    fun updateFoodNutrients(foodId: Long, updatedNutrientInfo: NutrientInfo) {
        val currentUiState = _uiState.value

        if (currentUiState.foodEditState is ResultState.Success) {
            val mealInfo = currentUiState.foodEditState.data

            val updatedItems = mealInfo.mealItems.map { item ->
                if (item.foodId == foodId) {
                    val newItem = item.copy(nutrientInfo = updatedNutrientInfo)
                    newItem
                } else {
                    item
                }
            }

            val updatedMealInfo = mealInfo.copy(mealItems = updatedItems)

            _uiState.update { currentState ->
                currentState.copy(
                    foodEditState = ResultState.Success(updatedMealInfo)
                )
            }

        }
    }

    fun selectFood(foodId: Long) {
        _uiState.update {
            it.copy(selectedFoodId = foodId)
        }
    }

}