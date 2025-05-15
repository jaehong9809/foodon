package com.swallaby.foodon.data.auth.remote.api

import com.swallaby.foodon.core.data.remote.BaseResponse
import com.swallaby.foodon.data.auth.remote.dto.request.KakaoLoginRequest
import com.swallaby.foodon.data.auth.remote.dto.request.TokenRequest
import com.swallaby.foodon.data.auth.remote.dto.response.KakaoLoginResponse
import com.swallaby.foodon.data.auth.remote.dto.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/kakao")
    suspend fun loginWithKakao(
        @Body request: KakaoLoginRequest
    ): BaseResponse<KakaoLoginResponse>

    @POST("auth/token/validate")
    suspend fun validateToken(
        @Body request: TokenRequest
    ): BaseResponse<TokenResponse>
}