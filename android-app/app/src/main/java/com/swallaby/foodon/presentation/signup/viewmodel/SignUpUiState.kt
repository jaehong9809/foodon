package com.swallaby.foodon.presentation.signup.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.domain.user.model.GenderOption

data class SignUpUiState(
    val selectedGender: GenderOption? = null,
    val managementType: Long? = null,
    val activityType: Long? = null,
    val weight: Int? = null,
    val goalWeight: Int? = null,
    val currentStep: Int = 1
): UiState
