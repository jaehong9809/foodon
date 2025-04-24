package com.swallaby.foodon.presentation.foodImage.viewmodel

import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.domain.food.usecase.UploadFoodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FoodImageViewModel @Inject constructor(
    private val uploadFoodUseCase: UploadFoodUseCase,
) : BaseViewModel<FoodImageUiState>(FoodImageUiState()) {

    fun uploadFoodImage() {
//        uploadFoodUseCase
    }
}