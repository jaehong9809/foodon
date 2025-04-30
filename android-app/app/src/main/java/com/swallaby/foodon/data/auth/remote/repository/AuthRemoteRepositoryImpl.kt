package com.swallaby.foodon.data.auth.remote.repository

import com.swallaby.foodon.core.data.remote.getOrThrow
import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.core.result.safeApiCall
import com.swallaby.foodon.data.auth.remote.api.AuthApi
import com.swallaby.foodon.data.auth.remote.dto.request.KakaoLoginRequest
import com.swallaby.foodon.data.auth.remote.dto.response.KakaoLoginResponse
import com.swallaby.foodon.domain.auth.repository.AuthRepository
import javax.inject.Inject

class AuthRemoteRepositoryImpl @Inject constructor(
    private val api: AuthApi
): AuthRepository {
    override suspend fun loginWithKakao(accessToken: String): ApiResult<KakaoLoginResponse> {
        return safeApiCall {
            api.loginWithKakao(KakaoLoginRequest(accessToken)).getOrThrow { it }
        }
    }
}
