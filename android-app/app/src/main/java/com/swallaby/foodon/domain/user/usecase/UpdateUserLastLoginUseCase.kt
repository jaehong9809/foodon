package com.swallaby.foodon.domain.user.usecase

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.domain.user.repository.UserRepository
import javax.inject.Inject

class UpdateUserLastLoginUseCase @Inject constructor (
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): ApiResult<Unit> {
        return userRepository.updateUserLastLogin()
    }
}