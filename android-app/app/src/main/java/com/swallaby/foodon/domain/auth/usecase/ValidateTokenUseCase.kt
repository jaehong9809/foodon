package com.swallaby.foodon.domain.auth.usecase

import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.data.auth.remote.dto.response.TokenResponse
import com.swallaby.foodon.domain.auth.repository.AuthRepository
import javax.inject.Inject

class ValidateTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(accessToken: String, refreshToken: String): ApiResult<TokenResponse> {
        return authRepository.validateToken(accessToken, refreshToken)
    }
}