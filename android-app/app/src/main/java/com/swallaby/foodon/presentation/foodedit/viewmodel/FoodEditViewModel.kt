package com.swallaby.foodon.presentation.foodedit.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.result.toResultState
import com.swallaby.foodon.data.food.remote.dto.request.CustomFoodRequest
import com.swallaby.foodon.domain.food.model.FoodInfoWithId
import com.swallaby.foodon.domain.food.model.FoodType
import com.swallaby.foodon.domain.food.model.MealInfo
import com.swallaby.foodon.domain.food.model.MealItem
import com.swallaby.foodon.domain.food.model.UnitType
import com.swallaby.foodon.domain.food.usecase.FetchFoodSimilarUseCase
import com.swallaby.foodon.domain.food.usecase.FetchFoodUseCase
import com.swallaby.foodon.domain.food.usecase.UpdateCustomFoodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodEditViewModel @Inject constructor(
    private val updateCustomFoodUseCase: UpdateCustomFoodUseCase,
    private val fetchFoodSimilarUseCase: FetchFoodSimilarUseCase,
    private val fetchFoodUseCase: FetchFoodUseCase,
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
            fetchFoodSimilar(sortedMealItems.first().foodName)

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

    fun fetchFoodSimilar(name: String) {
        _uiState.update {
            it.copy(foodSimilarState = ResultState.Loading)
        }
        viewModelScope.launch {
            _uiState.update {
                it.copy(foodSimilarState = fetchFoodSimilarUseCase(name).toResultState())
            }
        }
    }

    fun fetchFood(foodId: Long, type: FoodType) {
        val tempFoodId = _uiState.value.selectedFoodId
        // 가져오기 전에 먼저 선택
        _uiState.update {
            it.copy(
                selectedFoodId = foodId,
            )
        }
        viewModelScope.launch {
            when (val result = fetchFoodUseCase(foodId, type).toResultState()) {
                is ResultState.Success -> {
                    val food = result.data
                    val mealInfo = (_uiState.value.foodEditState as ResultState.Success).data
                    _uiState.update {
                        it.copy(
//                            selectedFoodId = food.foodId,
                            foodEditState = ResultState.Success(mealInfo.copy(mealItems = mealInfo.mealItems.map { item ->
                                if (item.foodId == tempFoodId) {
                                    Log.d("FoodEditViewModel", "food = $food")
                                    item.copy(
                                        foodId = food.foodId,
                                        foodName = food.foodName,
                                        nutrientInfo = food.nutrientInfo,
                                        unit = food.unit,
                                        type = food.type,
                                    )
                                } else {
                                    item
                                }
                            }))
                        )

                    }
                }

                is ResultState.Error -> {
                    Log.d("FoodEditViewModel", "Error fetchFood: ${result.messageRes}")
                }

                else -> {
                }
            }
        }
    }

    fun updateCustomFood(mealItem: MealItem) {
        viewModelScope.launch {
            val request = CustomFoodRequest(
                foodName = mealItem.foodName,
                nutrients = mealItem.nutrientInfo,
                servingSize = mealItem.servingSize.toInt(),
                unit = mealItem.unit
            )

            when (val result = updateCustomFoodUseCase(
                request = request
            ).toResultState()) {
                is ResultState.Success -> {
                    val updatedFood = result.data
                    Log.d("FoodEditViewModel", "Success custom food")
                    updateFoodNutrients(mealItem.foodId, updatedFood)
                    _events.emit(FoodEditEvent.SuccessCustomFood(mealItem))
                }

                is ResultState.Error -> {
                    Log.d("FoodEditViewModel", "Error updateCustomFood: ${result.messageRes}")
                    _events.emit(FoodEditEvent.FailedCustomFood(result.messageRes))
                }

                else -> {

                }

            }
        }

    }

    private fun updateFoodNutrients(
        foodId: Long,
        updatedFood: FoodInfoWithId,
    ) {
        val currentUiState = _uiState.value

        if (currentUiState.foodEditState is ResultState.Success) {
            val mealInfo = currentUiState.foodEditState.data

            val updatedItems = mealInfo.mealItems.map { item ->
                if (item.foodId == foodId) {
                    val newItem = item.copy(
                        foodId = updatedFood.foodId,
                        foodName = updatedFood.foodName,
                        unit = updatedFood.unit,
                        type = updatedFood.type,
                        quantity = item.quantity,
                        nutrientInfo = updatedFood.nutrientInfo,
                    )
                    newItem
                } else {
                    item
                }
            }

            val updatedMealInfo = mealInfo.copy(mealItems = updatedItems)

            _uiState.update { currentState ->
                currentState.copy(
                    foodEditState = ResultState.Success(updatedMealInfo),
                    selectedFoodId = updatedFood.foodId
                )
            }

        }
    }

    private fun updateFoodItem(foodId: Long, updateFunction: (item: MealItem) -> MealItem) {
        val currentUiState = _uiState.value

        if (currentUiState.foodEditState is ResultState.Success) {
            val mealInfo = currentUiState.foodEditState.data
            val updatedItems = mealInfo.mealItems.map { item ->
                if (item.foodId == foodId) {
                    updateFunction(item)
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

    fun updateUnitType(foodId: Long, unit: UnitType) {
        updateFoodItem(foodId) { item ->
            item.copy(unit = unit)
        }
    }

    fun updateQuantity(foodId: Long, quantity: Int) {
        updateFoodItem(foodId) { item ->
            item.copy(quantity = quantity)
        }
    }

    fun selectFood(foodId: Long, foodName: String) {
        fetchFoodSimilar(name = foodName)
        _uiState.update {
            it.copy(selectedFoodId = foodId)
        }
    }

}