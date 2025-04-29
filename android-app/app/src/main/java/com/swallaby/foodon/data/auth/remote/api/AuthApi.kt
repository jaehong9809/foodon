package com.swallaby.foodon.data.auth.remote.api

import com.swallaby.foodon.core.data.remote.BaseResponse
import com.swallaby.foodon.data.auth.remote.dto.request.KakaoLoginRequest
import com.swallaby.foodon.data.auth.remote.dto.response.KakaoLoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/kakao")
    suspend fun loginWithKakao(
        @Body request: KakaoLoginRequest
    ): BaseResponse<KakaoLoginResponse>

}