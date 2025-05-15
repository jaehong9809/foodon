package com.swallaby.foodon.presentation.mealrecord.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.R
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.result.toResultState
import com.swallaby.foodon.core.util.toMultipartBodyPart
import com.swallaby.foodon.domain.food.usecase.UploadMealUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class MealRecordViewModel @Inject constructor(
    private val uploadMealUseCase: UploadMealUseCase,
) : BaseViewModel<MealRecordUiState>(MealRecordUiState()) {

    private val _events = MutableSharedFlow<MealRecordEvent>()
    val events = _events.asSharedFlow()


    fun uploadMealImage(uri: Uri, context: Context) {
        val image = uri.toMultipartBodyPart(context)
        val validation = validateMultipartImageSize(image)
        Log.d("MealRecordViewModel", "validation: $validation")

        if (!validation.first) {
            _uiState.update {
                it.copy(failImageUpload = true)
            }
            viewModelScope.launch {
                _events.emit(
                    MealRecordEvent.ShowErrorMessage(R.string.over_size_image)
                )
            }
            return
        }


        _uiState.update {
            it.copy(mealRecordState = ResultState.Loading, failImageUpload = false)
        }
        viewModelScope.launch {
            val result = uploadMealUseCase(image).toResultState()
            _uiState.update { uiState ->
                uiState.copy(mealRecordState = result)
            }
            // 성공 시 이벤트 발생
            if (result is ResultState.Success) {
                Log.d("MealRecordViewModel", "isSuccess $result")
                _events.emit(MealRecordEvent.NavigateToDetail(result.data.copy(imageUri = uri)))
            } else if (result is ResultState.Error) {
                _events.emit(MealRecordEvent.ShowErrorMessage(result.messageRes))
            }
        }
    }

    fun resetMealRecordState() {
        _uiState.update {
            it.copy(mealRecordState = ResultState.Success(null))
        }
    }

    private fun validateMultipartImageSize(part: MultipartBody.Part): Pair<Boolean, Long> {
        val maxSizeBytes = 10 * 1024 * 1024 // 10MB in bytes
        val fileSize = getImageSizeFromMultipartBodyPart(part)
        return Pair(fileSize in 1..maxSizeBytes, fileSize)
    }

    private fun getImageSizeFromMultipartBodyPart(part: MultipartBody.Part): Long {
        try {
            val requestBody = part.body
            val contentLength = requestBody.contentLength()
            return contentLength
        } catch (e: Exception) {
            return 0L
        }
    }

}

