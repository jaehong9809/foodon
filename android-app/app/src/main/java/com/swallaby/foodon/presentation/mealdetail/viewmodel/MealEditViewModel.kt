package com.swallaby.foodon.presentation.mealdetail.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.result.toResultState
import com.swallaby.foodon.core.util.DateUtil
import com.swallaby.foodon.domain.food.model.FoodType
import com.swallaby.foodon.domain.food.model.MealInfo
import com.swallaby.foodon.domain.food.model.MealItem
import com.swallaby.foodon.domain.food.model.MealType
import com.swallaby.foodon.domain.food.model.toMealItem
import com.swallaby.foodon.domain.food.model.toRequest
import com.swallaby.foodon.domain.food.usecase.FetchFoodUseCase
import com.swallaby.foodon.domain.food.usecase.FetchMealDetailInfoUseCase
import com.swallaby.foodon.domain.food.usecase.RecordMealUseCase
import com.swallaby.foodon.presentation.sharedstate.MealSharedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import javax.inject.Inject
import androidx.core.net.toUri

@HiltViewModel
class MealEditViewModel @Inject constructor(
    private val recordMealUseCase: RecordMealUseCase,
    private val fetchMealDetailInfoUseCase: FetchMealDetailInfoUseCase,
    private val fetchFoodUseCase: FetchFoodUseCase,
    val mealSharedState: MealSharedState,
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
        }
    }

    fun initMeal(mealInfo: MealInfo) {
        Log.d(TAG, "Initializing MealEditViewModel")
        val uiState = _uiState.value.mealEditState
        val mealTimeType = if (uiState is ResultState.Success) {
            uiState.data.mealTimeType
        } else MealType.BREAKFAST
        Log.d("MealEditViewModel", "Meal time type: $mealTimeType")
        _uiState.update {
            it.copy(mealEditState = ResultState.Success(mealInfo.copy(mealTimeType = mealTimeType)))
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

                    mealSharedState.refreshForDaily()
                    mealSharedState.refreshForCalendar()
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

    fun updateMealItems(mealItems: List<MealItem>) {
        Log.d(TAG, "Updating meal items: ${mealItems.size}")
        val currentState = _uiState.value.mealEditState

        if (currentState is ResultState.Success) {
            _uiState.update { currentUiState ->
                currentUiState.copy(
                    mealEditState = ResultState.Success(
                        currentState.data.copy(
                            mealItems = mealItems,
                            totalCarbs = calculateTotalCarbs(mealItems),
                            totalFat = calculateTotalFat(mealItems),
                            totalKcal = calculateTotalKcal(mealItems),
                            totalProtein = calculateTotalProtein(mealItems)
                        )
                    )
                )
            }
        }

    }

    fun addFood(foodId: Long, type: FoodType = FoodType.PUBLIC, fromRecord: Boolean = false) {
        Log.d(TAG, "Adding food: $foodId, fromRecord = $fromRecord")

        viewModelScope.launch {
            when (val result = fetchFoodUseCase(foodId, type).toResultState()) {
                is ResultState.Success -> {
                    val food = result.data.toMealItem()
                    val mealInfo = (_uiState.value.mealEditState as ResultState.Success).data
                    Log.d("MealEditViewModel", "mealInfo = ${mealInfo.mealItems.size}")
                    val originFoodList = if (fromRecord) emptyList()
                    else mealInfo.mealItems.toMutableList()
                    val updatedItems = originFoodList + food
                    Log.d("MealEditViewModel", "updatedItems = ${updatedItems.size}")

                    val updatedMealInfo = mealInfo.copy(
                        mealItems = updatedItems,
                        totalCarbs = calculateTotalCarbs(updatedItems),
                        totalFat = calculateTotalFat(updatedItems),
                        totalKcal = calculateTotalKcal(updatedItems),
                        totalProtein = calculateTotalProtein(updatedItems),
                        mealTime = if (fromRecord) DateUtil.formatTimeToHHmm(LocalDateTime.now()) else mealInfo.mealTime,
                        imageUri = if (fromRecord)
                        // 기본 이미지
                            "https://foodon-bucket.s3.ap-northeast-2.amazonaws.com/images/0b4b1c06-dbf3-4259-882f-5b10d03657aa_20250520215957771237.png".toUri()
                        else mealInfo.imageUri,
                        imageFileName = if (fromRecord) "" else mealInfo.imageFileName
                    )

                    _uiState.update {
                        it.copy(
                            mealEditState = ResultState.Success(updatedMealInfo)
                        )
                    }
                }

                is ResultState.Error -> {
                }

                else -> {
                }
            }
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

