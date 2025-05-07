package com.swallaby.foodon.presentation.signup.viewmodel

import com.swallaby.foodon.core.presentation.UiState

data class SignUpUiState(
    val gender: String? = null,
    val managementType: Long? = null,
    val activityType: Long? = null,
    val weight: Int? = null,
    val goalWeight: Int? = null,
    val currentStep: Int = 1
): UiState
