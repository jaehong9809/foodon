package com.swallaby.foodon.domain.user.repository

import com.swallaby.foodon.domain.user.model.User

interface UserRepository {
    suspend fun getUserProfile(): User
}