package com.swallaby.foodon.presentation.mealdetail.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.result.toResultState
import com.swallaby.foodon.domain.food.model.MealInfo
import com.swallaby.foodon.domain.food.model.MealItem
import com.swallaby.foodon.domain.food.model.MealType
import com.swallaby.foodon.domain.food.model.toRequest
import com.swallaby.foodon.domain.food.usecase.FetchMealDetailInfoUseCase
import com.swallaby.foodon.domain.food.usecase.RecordMealUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealEditViewModel @Inject constructor(
    private val recordMealUseCase: RecordMealUseCase,
    private val fetchMealDetailInfoUseCase: FetchMealDetailInfoUseCase,
) : BaseViewModel<MealEditUiState>(MealEditUiState()) {
    private val _events = MutableSharedFlow<MealEditEvent>()
    val events = _events.asSharedFlow()

    init {
        Log.d("MealEditViewModel", "init called")
    }

    fun fetchMealDetailInfo(mealId: Long) {
        Log.d(TAG, "Fetching meal detail info for mealId: $mealId")
        _uiState.update {
            it.copy(mealEditState = ResultState.Loading)
        }
        viewModelScope.launch {
            _uiState.update {
                it.copy(mealEditState = fetchMealDetailInfoUseCase(mealId).toResultState())
            }
//            when (val result = fetchMealDetailInfoUseCase(mealId).toResultState()) {
//                is ResultState.Success -> {
//                    _uiState.update {
//                        it.copy(mealEditState = ResultState.Success(result.data))
//                    }
//                }
//
//                is ResultState.Error -> {
//                    _uiState.update {
//                        it.copy(mealEditState = ResultState.Error(result.messageRes))
//                    }
//                }
//
//                else -> {}
//            }
        }
    }

    fun initMeal(mealInfo: MealInfo) {
        Log.d(TAG, "Initializing MealEditViewModel")
        val uiState = _uiState.value.mealEditState
        if (uiState is ResultState.Success) {
            val mealTimeType = uiState.data.mealTimeType
            Log.d("MealEditViewModel", "Meal time type: $mealTimeType")
            _uiState.update {
                it.copy(mealEditState = ResultState.Success(mealInfo.copy(mealTimeType = mealTimeType)))
            }
        }
    }

    fun recordMeal() {
        val mealEditUiState = (_uiState.value.mealEditState as ResultState.Success)
        val mealInfo = mealEditUiState.data
        val request = mealInfo.toRequest().copy(
            mealTime = mealInfo.mealTime, mealTimeType = mealInfo.mealTimeType
        )
        viewModelScope.launch {
            when (val result = recordMealUseCase(request).toResultState()) {
                is ResultState.Success -> {
                    _uiState.update { it.copy(mealEditState = ResultState.Success(mealInfo)) }
                    _events.emit(MealEditEvent.NavigateToMain)
                }

                is ResultState.Error -> {
                    _events.emit(MealEditEvent.ShowErrorMessage(result.messageRes))
//                    _uiState.update { it.copy(mealEditState = ResultState.Error(messageRes = result.messageRes)) }
                }

                else -> {
                    _uiState.update { it.copy(mealEditState = ResultState.Loading) }
                }
            }
        }
    }

    fun updateMealType(mealType: MealType) {
        Log.d(TAG, "Updating meal type: $mealType")

        val currentState = _uiState.value.mealEditState
        if (currentState is ResultState.Success) {
            _uiState.update { currentUiState ->
                currentUiState.copy(
                    mealEditState = ResultState.Success(
                        currentState.data.copy(
                            mealTimeType = mealType
                        )
                    )
                )
            }
        }
    }

    fun updateMealTime(mealTime: String) {
        Log.d(TAG, "Updating meal time: $mealTime")

        val currentState = _uiState.value.mealEditState
        if (currentState is ResultState.Success) {
            _uiState.update { currentUiState ->
                currentUiState.copy(
                    mealEditState = ResultState.Success(
                        currentState.data.copy(
                            mealTime = mealTime
                        )
                    )
                )
            }
        }
    }

    fun updateFood(food: MealItem) {
        Log.d(TAG, "Updating food: ${food.foodId}")
        val currentState = _uiState.value.mealEditState
        if (currentState is ResultState.Success) {
            val mealInfo = currentState.data
            val updatedItems = mealInfo.mealItems.map {
                if (it.foodId == food.foodId) food else it
            }

            _uiState.update {
                it.copy(
                    mealEditState = ResultState.Success(
                        mealInfo.copy(
                            mealItems = updatedItems,
                            totalCarbs = calculateTotalCarbs(updatedItems),
                            totalFat = calculateTotalFat(updatedItems),
                            totalKcal = calculateTotalKcal(updatedItems),
                            totalProtein = calculateTotalProtein(updatedItems)
                        )
                    )
                )
            }
            Log.d(TAG, "Food update complete: ${food.foodId}")
        } else {
            Log.e(TAG, "Cannot update food: Invalid state")
        }
    }

    fun deleteFood(foodId: Long) {
        Log.d(TAG, "Deleting food: $foodId")
        val currentState = _uiState.value.mealEditState
        if (currentState is ResultState.Success) {
            val mealInfo = currentState.data
            val updatedItems = mealInfo.mealItems.filterNot { it.foodId == foodId }

            val updatedMealInfo = mealInfo.copy(
                mealItems = updatedItems,
                totalCarbs = calculateTotalCarbs(updatedItems),
                totalFat = calculateTotalFat(updatedItems),
                totalKcal = calculateTotalKcal(updatedItems),
                totalProtein = calculateTotalProtein(updatedItems)
            )

            _uiState.update { it.copy(mealEditState = ResultState.Success(updatedMealInfo)) }
            Log.d(TAG, "Food deletion complete: $foodId")
        } else {
            Log.e(TAG, "Cannot delete food: Invalid state")
        }
    }

    fun destroyMeal() {
        _uiState.update { it.copy(mealEditState = ResultState.Success(MealInfo())) }
    }

    private fun calculateTotalCarbs(items: List<MealItem>): Double {
        return items.sumOf { it.nutrientInfo.carbs }
    }

    private fun calculateTotalFat(items: List<MealItem>): Double {
        return items.sumOf { it.nutrientInfo.fat }
    }

    private fun calculateTotalKcal(items: List<MealItem>): Int {
        return items.sumOf { it.nutrientInfo.kcal }
    }

    private fun calculateTotalProtein(items: List<MealItem>): Double {
        return items.sumOf { it.nutrientInfo.protein }
    }

    companion object {
        private const val TAG = "MealEditViewModel"
    }
}

