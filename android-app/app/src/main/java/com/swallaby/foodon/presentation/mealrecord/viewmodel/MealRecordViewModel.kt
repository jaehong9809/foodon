package com.swallaby.foodon.presentation.mealrecord.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
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
import javax.inject.Inject

@HiltViewModel
class MealRecordViewModel @Inject constructor(
    private val uploadMealUseCase: UploadMealUseCase,
) : BaseViewModel<MealRecordUiState>(MealRecordUiState()) {

    private val _events = MutableSharedFlow<MealRecordEvent>()
    val events = _events.asSharedFlow()

    fun uploadMealImage(uri: Uri, context: Context) {
        val image = uri.toMultipartBodyPart(context)

        _uiState.update {
            it.copy(mealRecordState = ResultState.Loading)
        }
        viewModelScope.launch {
            val result = uploadMealUseCase(image).toResultState()
            _uiState.update { uiState ->
                uiState.copy(mealRecordState = result)
            }
            // 성공 시 이벤트 발생
            if (result is ResultState.Success) {
                _events.emit(MealRecordEvent.NavigateToDetail(result.data.copy(imageUri = uri)))
            }
        }
    }

    fun resetMealRecordState() {
        _uiState.update {
            it.copy(mealRecordState = ResultState.Success(null))
        }
    }

}

