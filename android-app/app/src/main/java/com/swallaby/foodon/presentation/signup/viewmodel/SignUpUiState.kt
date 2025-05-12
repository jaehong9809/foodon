package com.swallaby.foodon.presentation.signup.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.domain.user.model.GenderOption
import com.swallaby.foodon.domain.user.model.ManagementTypeOption

data class SignUpUiState(
    val selectedGender: GenderOption? = null,
    val managementOptions: List<ManagementTypeOption> = emptyList(),
    val selectedManagementTypeId: Long? = null,
    val activityType: Long? = null,
    val weight: Int? = null,
    val goalWeight: Int? = null,
    val currentStep: Int = 1
): UiState
