package com.swallaby.foodon.presentation.login.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.core.result.ResultState

data class LoginUiState(
    val loginResult: ResultState<Unit> = ResultState.Loading
): UiState
