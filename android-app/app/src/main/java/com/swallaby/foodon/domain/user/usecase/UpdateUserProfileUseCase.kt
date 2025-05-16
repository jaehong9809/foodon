package com.swallaby.foodon.domain.user.usecase

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.data.user.remote.dto.request.UpdateProfileRequest
import com.swallaby.foodon.domain.user.repository.UserRepository
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor (
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(request: UpdateProfileRequest): ApiResult<Unit> {
        return userRepository.updateUserProfile(request)
    }
}