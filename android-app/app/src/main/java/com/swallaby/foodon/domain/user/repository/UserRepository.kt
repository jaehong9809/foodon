package com.swallaby.foodon.domain.user.repository

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.data.user.remote.dto.request.UpdateProfileRequest
import com.swallaby.foodon.domain.user.model.User

interface UserRepository {
    suspend fun getUserProfile(): User
    suspend fun updateUserProfile(request: UpdateProfileRequest): ApiResult<Unit>
}