package com.swallaby.foodon.presentation.login.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.R

import com.swallaby.foodon.core.data.TokenDataStore
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.data.auth.remote.result.AuthFlowResult
import com.swallaby.foodon.domain.auth.usecase.LoginWithKakaoUseCase
import com.swallaby.foodon.domain.user.usecase.UpdateUserLastLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWithKakaoUseCase: LoginWithKakaoUseCase,
    private val updateUserLastLoginUseCase: UpdateUserLastLoginUseCase,
    private val tokenDataStore: TokenDataStore
) : BaseViewModel<LoginUiState>(LoginUiState()) {

    fun loginWithKakao(kakaoAccessToken: String) {
        _uiState.value = _uiState.value.copy(loginResult = ResultState.Loading)

        viewModelScope.launch {
            val result = loginWithKakaoUseCase(kakaoAccessToken)
            when (result) {
                is ApiResult.Success -> {
                    val access = result.data.accessToken
                    val refresh = result.data.refreshToken
                    val profileUpdated = result.data.profileUpdated

                    tokenDataStore.saveTokens(access, refresh)

                    launch {
                        runCatching { updateUserLastLoginUseCase() }
                            .onFailure { e -> Log.e("Last Login (POST API)", "로그인 시간 Update 실패", e) }
                    }

                    val flowResult = if (profileUpdated) {
                        AuthFlowResult.NavigateToMain
                    } else {
                        AuthFlowResult.NavigateToSignUp
                    }
                    _uiState.value = _uiState.value.copy(loginResult = ResultState.Success(flowResult))
                }

                is ApiResult.Failure -> {
                    _uiState.value = _uiState.value.copy(loginResult = ResultState.Error(R.string.error_unknown))
                }
            }
        }
    }
}
