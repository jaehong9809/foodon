package com.swallaby.foodon.presentation.login

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.swallaby.foodon.presentation.login.component.KakaoLoginButton

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            KakaoLoginButton(
                onClick = {
                    startKakaoLogin(context, onLoginSuccess)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.8f)
            )
        }
    }
}

private fun startKakaoLogin(
    context: android.content.Context,
    onLoginSuccess: (String) -> Unit
) {
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        when {
            error != null -> {
                Log.e("LoginScreen", "카카오 로그인 실패", error)
            }
            token != null -> {
                Log.i("LoginScreen", "카카오 로그인 성공: ${token.accessToken}")
                onLoginSuccess(token.accessToken)
            }
            else -> {
                Log.e("LoginScreen", "카카오 로그인 실패: 알 수 없는 오류")
            }
        }
    }

    if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
        UserApiClient.instance.loginWithKakaoTalk(context = context, callback = callback)
    } else {
        UserApiClient.instance.loginWithKakaoAccount(context = context, callback = callback)
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onLoginSuccess = {}
    )
}
