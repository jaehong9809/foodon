package com.swallaby.foodon.presentation.login.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.domain.user.model.User

data class UserProfileUiState(
    val userState: ResultState<User> = ResultState.Loading
): UiState
