package com.swallaby.foodon.domain.auth.repository

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.data.auth.remote.dto.response.KakaoLoginResponse


interface AuthRepository {
    suspend fun loginWithKakao(accessToken: String) : ApiResult<KakaoLoginResponse>
}