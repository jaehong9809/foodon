package com.swallaby.foodon.domain.auth.usecase

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.data.auth.remote.dto.response.KakaoLoginResponse
import com.swallaby.foodon.data.auth.remote.dto.response.TokenResponse
import com.swallaby.foodon.domain.auth.repository.AuthRepository
import javax.inject.Inject

class LoginWithKakaoUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(accessToken: String): ApiResult<TokenResponse> {
        return authRepository.loginWithKakao(accessToken)
    }
}
