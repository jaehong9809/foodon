package com.swallaby.foodon.presentation.login.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.swallaby.foodon.domain.auth.usecase.LoginWithKakaoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWithKakaoUseCase: LoginWithKakaoUseCase
) : ViewModel() {

    fun startKakaoLogin(
        context: Context,
        onSuccess: (String) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            when {
                error != null -> {
                    Log.e("LoginViewModel", "카카오 로그인 실패", error)
                    onFailure(error)
                }
                token != null -> {
                    Log.i("LoginViewModel", "카카오 로그인 성공: ${token.accessToken}")
                    onSuccess(token.accessToken)
                }
                else -> {
                    Log.e("LoginViewModel", "카카오 로그인 실패: 알 수 없는 오류")
                    onFailure(IllegalStateException("Unknown login error"))
                }
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context = context, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context = context, callback = callback)
        }
    }

}