package com.swallaby.foodon.presentation.login.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.swallaby.foodon.core.data.TokenDataStore
import com.swallaby.foodon.core.error.AppError
import com.swallaby.foodon.core.result.ApiResult
import com.swallaby.foodon.data.auth.remote.dto.response.KakaoLoginResponse
import com.swallaby.foodon.domain.auth.usecase.LoginWithKakaoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWithKakaoUseCase: LoginWithKakaoUseCase,
    private val tokenDataStore: TokenDataStore
) : ViewModel() {

    fun startKakaoLogin(
        context: Context,
        onSuccess: () -> Unit,
        onFailure: (AppError) -> Unit
    ) {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            when {
                error != null -> {
                    Log.e("LoginViewModel", "카카오 로그인 실패", error)
                    onFailure(AppError.Unknown(error))
                }
                token != null -> {
                    Log.i("LoginViewModel", "카카오 로그인 성공: ${token.accessToken}")
                    loginToServer(token.accessToken, context, onSuccess, onFailure)
                }
                else -> {
                    onFailure(AppError.Unknown(IllegalStateException("알 수 없는 로그인 오류")))
                }
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context = context, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context = context, callback = callback)
        }
    }

    private fun loginToServer(
        kakaoAccessToken: String,
        context: Context,
        onSuccess: () -> Unit,
        onFailure: (AppError) -> Unit
    ) {
        viewModelScope.launch {
            when (val result = loginWithKakaoUseCase(kakaoAccessToken)) {
                is ApiResult.Success -> {
                    tokenDataStore.saveTokens(
                        accessToken = result.data.accessToken,
                        refreshToken = result.data.refreshToken
                    )
                    Toast.makeText(context, "로그인 성공!", Toast.LENGTH_SHORT).show()
                    onSuccess()
                }
                is ApiResult.Failure -> {
                    Toast.makeText(context, "서버 로그인 실패", Toast.LENGTH_SHORT).show()
                    onFailure(result.error)
                }
            }
        }
    }
}