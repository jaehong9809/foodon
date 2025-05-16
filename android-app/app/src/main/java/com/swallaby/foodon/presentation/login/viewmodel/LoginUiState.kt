package com.swallaby.foodon.presentation.login.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.data.auth.remote.result.AuthFlowResult

data class LoginUiState(
    val loginResult: ResultState<AuthFlowResult> = ResultState.Loading
): UiState
