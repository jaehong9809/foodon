package com.swallaby.foodon.presentation.splash.viewmodel

import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.R
import com.swallaby.foodon.core.data.TokenDataStore
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.data.auth.remote.result.AuthFlowResult
import com.swallaby.foodon.domain.auth.usecase.ValidateTokenUseCase
import com.swallaby.foodon.presentation.sharedstate.AppSharedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val validateTokenUseCase: ValidateTokenUseCase,
    private val tokenDataStore: TokenDataStore,
    private val appSharedState: AppSharedState,
) : BaseViewModel<SplashUiState>(SplashUiState()) {

    init {
        viewModelScope.launch {
            val access = tokenDataStore.accessTokenFlow.firstOrNull()
            val refresh = tokenDataStore.refreshTokenFlow.firstOrNull()

            if (access.isNullOrBlank() || refresh.isNullOrBlank()) {
                _uiState.value = SplashUiState(ResultState.Error(R.string.error_invalid_token))
                return@launch
            }

            when (val result = validateTokenUseCase(access, refresh)) {
                is ApiResult.Success -> {
                    val newAccess = result.data.accessToken
                    val newRefresh = result.data.refreshToken
                    val profileUpdated = result.data.profileUpdated

                    tokenDataStore.saveTokens(newAccess, newRefresh)

                    val next = if (profileUpdated) {
                        AuthFlowResult.NavigateToMain
                    } else {
                        AuthFlowResult.NavigateToSignUp
                    }
                    _uiState.value = SplashUiState(ResultState.Success(next))

                    appSharedState.observeToken(tokenDataStore)
                }

                is ApiResult.Failure -> {
                    tokenDataStore.clearTokens()
                    _uiState.value = SplashUiState(ResultState.Error(R.string.error_invalid_token))
                }
            }
        }
    }
}
