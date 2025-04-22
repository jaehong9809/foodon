package com.swallaby.foodon.domain.user.usecase

import com.swallaby.foodon.domain.user.model.User
import com.swallaby.foodon.domain.user.repository.UserRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): User {
        return userRepository.getUserProfile()
    }
}
