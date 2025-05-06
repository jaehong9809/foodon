package com.swallaby.foodon.presentation.foodrecord.viewmodel

import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.domain.food.usecase.UploadFoodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FoodRecordViewModel @Inject constructor(
    private val uploadFoodUseCase: UploadFoodUseCase,
) : BaseViewModel<FoodRecordUiState>(FoodRecordUiState()) {

    fun uploadFoodImage() {
//        uploadFoodUseCase
    }
}