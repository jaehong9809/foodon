package com.swallaby.foodon.presentation.signup.viewmodel

import com.swallaby.foodon.core.presentation.UiState
import com.swallaby.foodon.domain.user.model.ActivityTypeOption
import com.swallaby.foodon.domain.user.model.GenderOption
import com.swallaby.foodon.domain.user.model.ManagementTypeOption

data class SignUpUiState(
    val selectedGender: GenderOption? = null,

    val managementOptions: List<ManagementTypeOption> = emptyList(),
    val selectedManagementTypeId: Long? = null,

    val activityTypeOptions: List<ActivityTypeOption> = emptyList(),
    val selectedActivityTypeId: Long? = null,

    val height: Int = 170,
    val weight: Int = 65,
    val goalWeight: Int? = null,

    val currentStep: Int = 1
): UiState
