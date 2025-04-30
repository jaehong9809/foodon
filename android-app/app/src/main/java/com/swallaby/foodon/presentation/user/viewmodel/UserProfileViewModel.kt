package com.swallaby.foodon.presentation.user.viewmodel

import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.result.safeApiCall
import com.swallaby.foodon.core.result.toResultState
import com.swallaby.foodon.domain.user.usecase.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase
) : BaseViewModel<UserProfileUiState>(UserProfileUiState()) {

    fun fetchUserProfile() {
        _uiState.value = _uiState.value.copy(userState = ResultState.Loading)

        viewModelScope.launch {
            val result = safeApiCall { getUserProfileUseCase() }
            _uiState.value = _uiState.value.copy(userState = result.toResultState())
        }
    }
}