package com.swallaby.foodon.domain.auth.repository

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.data.auth.remote.dto.response.KakaoLoginResponse
import com.swallaby.foodon.data.auth.remote.dto.response.TokenResponse


interface AuthRepository {
    suspend fun loginWithKakao(accessToken: String) : ApiResult<TokenResponse>
    suspend fun validateToken(accessToken: String, refreshToken: String): ApiResult<TokenResponse>
}