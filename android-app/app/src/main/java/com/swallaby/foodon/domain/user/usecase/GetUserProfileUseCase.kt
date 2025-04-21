package com.swallaby.foodon.domain.user.usecase

import com.swallaby.foodon.domain.user.model.User
import com.swallaby.foodon.domain.user.repository.UserRepository

class GetUserProfileUseCase (
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): User {
        return userRepository.getUserProfile()
    }
}
