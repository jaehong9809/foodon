package com.swallaby.foodon.data.auth.remote.repository

import com.swallaby.foodon.data.auth.remote.api.AuthApi
import com.swallaby.foodon.domain.auth.repository.AuthRepository
import javax.inject.Inject

class AuthRemoteRepositoryImpl @Inject constructor(
    private val api: AuthApi
): AuthRepository {
    override suspend fun loginWithKakao(): Boolean {
        TODO("Not yet implemented")
    }
}