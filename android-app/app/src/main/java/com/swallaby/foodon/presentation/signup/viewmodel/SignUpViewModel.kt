package com.swallaby.foodon.presentation.signup.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewModelScope
import com.swallaby.foodon.core.presentation.BaseViewModel
import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.data.user.remote.dto.request.UpdateProfileRequest
import com.swallaby.foodon.domain.user.model.ActivityTypeOption
import com.swallaby.foodon.domain.user.model.GenderOption
import com.swallaby.foodon.domain.user.model.ManagementTypeOption
import com.swallaby.foodon.domain.user.usecase.UpdateUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val updateUserProfileUseCase: UpdateUserProfileUseCase
) : BaseViewModel<SignUpUiState>(SignUpUiState()) {

    init { // TODO: UI를 위한 Init, 추후 API 붙이면 삭제
        val managementTypeList = listOf(
            ManagementTypeOption(
                id = 1L,
                title = "고단백형",
                description = "근손실 최소화와 체지방 감량, 운동 병행에 적합"
            ),
            ManagementTypeOption(
                id = 2L,
                title = "저당형",
                description = "혈당 스파이크 방지, 인슐린 저항 개선"
            ),
            ManagementTypeOption(
                id = 3L,
                title = "저나트륨형",
                description = "고혈압, 부종 개선에 도움"
            ),
            ManagementTypeOption(
                id = 4L,
                title = "건강 유지형",
                description = "균형잡힌 식단으로 유지 또는 초보자에게 적합"
            )
        )

        val activityTypeList = listOf(
            ActivityTypeOption(id = 1L, title = "주로 앉아서 생활, 별도 운동 없음"),
            ActivityTypeOption(id = 2L, title = "가벼운 활동 또는 주 1~3회 운동"),
            ActivityTypeOption(id = 3L, title = "활동적인 직업 또는 주 4회 이상 운동")
        )

        _uiState.update { it.copy(
                managementOptions = managementTypeList,
                activityTypeOptions = activityTypeList,
            )
        }
    }

    fun updateState(block: (SignUpUiState) -> SignUpUiState) {
        _uiState.update(block)
    }

    fun selectGender(option: GenderOption) {
        updateState { it.copy(selectedGender = option) }
    }

    fun setManagementOptions(options: List<ManagementTypeOption>) {
        updateState { it.copy(managementOptions = options) }
    }

    fun selectManagementType(id: Long) {
        updateState { it.copy(selectedManagementTypeId = id) }
    }

    fun setActivityOptions(options: List<ActivityTypeOption>) {
        updateState { it.copy(activityTypeOptions = options) }
    }

    fun selectActivityType(id: Long) {
        updateState { it.copy(selectedActivityTypeId = id) }
    }

    fun onHeightChange(newHeight: Int) {
        updateState { it.copy(height = newHeight) }
    }

    fun onWeightChange(newWeight: Int) {
        updateState { it.copy(weight = newWeight) }
    }

    fun onGoalWeightChange(newGoal: Int) {
        updateState { it.copy(goalWeight = newGoal) }
    }

    fun submitProfile(
        onSuccess: () -> Unit,
        onError: (Int) -> Unit
    ) {
        val state = uiState.value
        val gender = state.selectedGender?.textValue?: return
        val managementType = state.selectedManagementTypeId ?: return
        val activityType = state.selectedActivityTypeId ?: return
        val height = state.height ?: return
        val weight = state.weight ?: return
        val goalWeight = state.goalWeight ?: return

        val request = UpdateProfileRequest(
            gender = gender,
            managementType = managementType.toInt(),
            activityType = activityType.toInt(),
            height = height,
            weight = weight,
            goalWeight = goalWeight
        )

        viewModelScope.launch {
            when (val result = updateUserProfileUseCase(request)) {
                is ApiResult.Success -> onSuccess()
                is ApiResult.Failure -> onError(result.error.messageRes)
            }
        }
    }
}