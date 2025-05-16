package com.swallaby.foodon.presentation.splash.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.data.auth.remote.result.AuthFlowResult

data class SplashUiState(
    val result: ResultState<AuthFlowResult> = ResultState.Loading
) : UiState
