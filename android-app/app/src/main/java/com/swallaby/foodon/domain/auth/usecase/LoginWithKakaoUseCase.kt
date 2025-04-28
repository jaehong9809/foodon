package com.swallaby.foodon.domain.auth.usecase

import com.swallaby.foodon.domain.auth.repository.AuthRepository
import javax.inject.Inject

class LoginWithKakaoUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    // TODO: 추후 문서 보고 필요한 param 추가
    suspend operator fun invoke(): Boolean {
        return authRepository.loginWithKakao()
    }
}
