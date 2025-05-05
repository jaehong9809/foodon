package com.swallaby.foodon.presentation.foodedit.viewmodel

import android.util.Log
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.food.model.MealInfo
import com.swallaby.foodon.domain.food.model.MealItem
import com.swallaby.foodon.domain.food.model.NutrientInfo
import com.swallaby.foodon.presentation.mealdetail.viewmodel.createDummyMealInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FoodEditViewModel @Inject constructor() : BaseViewModel<FoodEditUiState>(FoodEditUiState()) {
    private var isInitialized = false

    init {
        Log.d("FoodEditViewModel", "init: ")
    }

    override fun onCleared() {
        Log.d("FoodEditViewModel", "onCleared: ")
        super.onCleared()
    }

    fun initFood(mealInfo: MealInfo) {
        if (!isInitialized) {
            _uiState.update {
                it.copy(foodEditState = ResultState.Success(mealInfo))
            }
            isInitialized = true
        }
    }

    fun updateFood(food: MealItem) {
        val currentState = _uiState.value.foodEditState
        if (currentState is ResultState.Success) {
            val mealInfo = currentState.data
            val updatedItems = mealInfo.mealItems.map {
                if (it.foodId == food.foodId) {
                    food
                } else {
                    it
                }
            }
            _uiState.update {
                it.copy(
                    foodEditState = ResultState.Success(
                        mealInfo.copy(mealItems = updatedItems)
                    )
                )
            }
        }
    }

    //    fun updateFoodNutrients(foodId: Long, nutrientInfo: NutrientInfo) {
//        Log.d("FoodEditViewModel", "updateFoodNutrients: $foodId $nutrientInfo")
//
//        val currentState = _uiState.value.foodEditState
//        if (currentState is ResultState.Success) {
//            val mealInfo = currentState.data
//            val updatedItems = mealInfo.mealItems.map {
//                if (it.foodId == foodId) {
//                    it.copy(nutrientInfo = nutrientInfo)
//                } else {
//                    it
//                }
//            }
//            _uiState.update {
//                it.copy(
//                    foodEditState = ResultState.Success(
//                        mealInfo.copy(mealItems = updatedItems)
//                    )
//                )
//            }
//
//            val updateNutritionInfo =
//                (_uiState.value.foodEditState as ResultState.Success<MealInfo>).data.mealItems.find { item -> item.foodId == foodId }!!.nutrientInfo
//            Log.d("FoodEditViewModel", "updateFoodNutrients: $updateNutritionInfo")
//
//        }
//    }


    fun updateFoodNutrients(foodId: Long, updatedNutrientInfo: NutrientInfo) {
        val currentUiState = _uiState.value
        if (currentUiState.foodEditState is ResultState.Success) {
            val mealInfo = currentUiState.foodEditState.data

            Log.d(
                "FoodEditViewModel",
                "Before update: ${mealInfo.mealItems.find { it.foodId == foodId }?.nutrientInfo}"
            )

            // 해당 음식을 찾아 영양소 정보 업데이트 (반드시 새 객체 생성)
            val updatedItems = mealInfo.mealItems.map { item ->
                if (item.foodId == foodId) {
                    // 새 객체 생성 확인
                    val newItem = item.copy(nutrientInfo = updatedNutrientInfo)
                    Log.d("FoodEditViewModel", "New item created: $newItem")
                    newItem
                } else {
                    item
                }
            }

            // 업데이트된 mealInfo 생성 (새 객체)
            val updatedMealInfo = mealInfo.copy(mealItems = updatedItems)

            Log.d(
                "FoodEditViewModel",
                "After update: ${updatedMealInfo.mealItems.find { it.foodId == foodId }?.nutrientInfo}"
            )

            // UI 상태 업데이트 (새 객체)
            _uiState.update { currentState ->
                currentState.copy(
                    foodEditState = ResultState.Success(updatedMealInfo)
                )
            }

            // 업데이트 후 현재 상태 확인
            val afterUpdateState = _uiState.value
            if (afterUpdateState.foodEditState is ResultState.Success) {
                val updatedFood =
                    afterUpdateState.foodEditState.data.mealItems.find { it.foodId == foodId }
                Log.d("FoodEditViewModel", "After _uiState update: ${updatedFood?.nutrientInfo}")
            }
        }
    }

}