package com.swallaby.foodon.data.user.remote.repository

import com.swallaby.foodon.core.data.remote.getOrThrow
import com.swallaby.foodon.core.data.remote.getOrThrowNull
import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.core.result.safeApiCall
import com.swallaby.foodon.data.user.remote.api.UserApi
import com.swallaby.foodon.data.user.remote.dto.request.UpdateProfileRequest
import com.swallaby.foodon.data.user.remote.dto.response.toDomain
import com.swallaby.foodon.domain.user.model.User
import com.swallaby.foodon.domain.user.repository.UserRepository
import javax.inject.Inject

class UserRemoteRepositoryImpl @Inject constructor(
    private val api: UserApi
) : UserRepository {

    override suspend fun getUserProfile(): User {
        return api.getUserProfile().getOrThrow { it.toDomain() }
    }

    override suspend fun updateUserProfile(request: UpdateProfileRequest): ApiResult<Unit> {
        return safeApiCall {
            api.updateUserProfile(request).getOrThrowNull { }
        }
    }
}