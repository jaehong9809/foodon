package com.swallaby.foodon.presentation.login.viewmodel

import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.R

import com.swallaby.foodon.core.data.TokenDataStore
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.auth.usecase.LoginWithKakaoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWithKakaoUseCase: LoginWithKakaoUseCase,
    private val tokenDataStore: TokenDataStore
) : BaseViewModel<LoginUiState>(LoginUiState()) {

    fun loginWithKakao(kakaoAccessToken: String) {
        _uiState.value = _uiState.value.copy(loginResult = ResultState.Loading)

        viewModelScope.launch {
            val result = loginWithKakaoUseCase(kakaoAccessToken)
            when (result) {
                is ApiResult.Success -> {
                    tokenDataStore.saveTokens(
                        accessToken = result.data.accessToken,
                        refreshToken = result.data.refreshToken
                    )
                    _uiState.value = _uiState.value.copy(loginResult = ResultState.Success(Unit))
                }

                is ApiResult.Failure -> {
                    _uiState.value = _uiState.value.copy(loginResult = ResultState.Error(R.string.error_unknown))
                }
            }
        }
    }
}
