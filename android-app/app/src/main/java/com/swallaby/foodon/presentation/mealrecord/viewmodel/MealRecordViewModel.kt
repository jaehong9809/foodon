package com.swallaby.foodon.presentation.mealrecord.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.R
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.result.toResultState
import com.swallaby.foodon.core.util.ImageConverter
import com.swallaby.foodon.core.util.ImageUtil.validateImage
import com.swallaby.foodon.core.util.toMultipartBodyPart
import com.swallaby.foodon.domain.food.model.MealInfo
import com.swallaby.foodon.domain.food.usecase.UploadMealUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MealRecordViewModel @Inject constructor(
    private val uploadMealUseCase: UploadMealUseCase,
) : BaseViewModel<MealRecordUiState>(MealRecordUiState()) {

    private val _events = MutableSharedFlow<MealRecordEvent>()
    val events = _events.asSharedFlow()

    fun capture() {
        _uiState.update {
            it.copy(mealRecordState = ResultState.Loading, imageUploadFailMessage = null)
        }
    }

    fun clearImageUploadFailMessage() {
        _uiState.update {
            it.copy(imageUploadFailMessage = null)
        }
    }

    fun uploadMealImage(uri: Uri, context: Context) {
        // 먼저 URI에서 직접 이미지 검증
        val imageValidation = validateImage(uri, context)

        if (imageValidation.first) {
            // 유효한 이미지는 바로 업로드
            upload(uri, context)
            return
        }

        // 이미지 크기 문제인 경우 WebP 변환 시도
        if (imageValidation.second == R.string.invalid_image_size) {
            handleLargeImage(uri, context, imageValidation.second)
        } else {
            // 기타 검증 오류 - 에러 메시지 표시
            emitErrorEvent(imageValidation.second)
        }
    }

    private fun handleLargeImage(uri: Uri, context: Context, errorMessageRes: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // WebP로 변환 시도
                val webpFile = ImageConverter.convertUriToWebP(context, uri, 85)
                val webpUri = webpFile?.let { Uri.fromFile(it) }

                when {
                    webpUri != null -> {
                        // 변환 성공, 변환된 이미지 재검증
                        val webpValidation = validateImage(webpUri, context)
                        if (webpValidation.first) {
                            upload(webpUri, context)
                        } else {
                            // 변환 후에도 검증 실패
                            emitErrorEvent(webpValidation.second)
                        }
                    }

                    else -> {
                        // 변환 실패
                        emitErrorEvent(errorMessageRes)
                    }
                }
            } catch (e: Exception) {
                Log.e("MealRecordViewModel", "WebP 변환 중 오류 발생", e)
                emitErrorEvent(errorMessageRes) // 일반 오류 메시지 리소스 ID로 대체
            }
        }
    }


    private fun emitErrorEvent(messageRes: Int) {
        viewModelScope.launch {
            _events.emit(MealRecordEvent.ShowErrorMessage(messageRes))
            _uiState.update {
                it.copy(
                    mealRecordState = ResultState.Error(messageRes),
                    imageUploadFailMessage = messageRes
                )
            }
        }
    }

    private fun upload(uri: Uri, context: Context) {
        viewModelScope.launch {
            val image = uri.toMultipartBodyPart(context)
            val result = uploadMealUseCase(image).toResultState()

            _uiState.update { uiState ->
                uiState.copy(mealRecordState = result)
            }

            handleUploadResult(result, uri)
        }
    }

    private fun handleUploadResult(result: ResultState<MealInfo>, uri: Uri) {
        viewModelScope.launch {
            when (result) {
                is ResultState.Success -> {
                    Log.d("MealRecordViewModel", "Upload successful: $result")
                    _events.emit(MealRecordEvent.NavigateToDetail(result.data.copy(imageUri = uri)))
                }

                is ResultState.Error -> {
                    emitErrorEvent(result.messageRes)
                }

                else -> {
                    // 기타 상태 처리 (필요시)
                }
            }
        }
    }

    fun resetMealRecordState() {
        _uiState.update {
            it.copy(mealRecordState = ResultState.Success(null))
        }
    }

}

