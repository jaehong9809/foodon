package com.swallaby.foodon.presentation.splash.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.core.result.ResultState

data class SplashUiState(
    val result: ResultState<Unit> = ResultState.Loading
) : UiState
