package com.swallaby.foodon.presentation.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.presentation.login.component.KakaoLoginButton
import com.swallaby.foodon.presentation.login.viewmodel.LoginViewModel
import com.swallaby.foodon.presentation.navigation.LocalNavController

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val navController = LocalNavController.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Kakao SDK Callback
    val kakaoLoginCallback = remember {
        { token: OAuthToken?, error: Throwable? ->
            when {
                error != null -> {
                    Toast.makeText(context, "카카오 로그인 실패", Toast.LENGTH_SHORT).show()
                }
                token != null -> {
                    viewModel.loginWithKakao(token.accessToken)
                }
                else -> {
                    Toast.makeText(context, "알 수 없는 오류", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            KakaoLoginButton(
                onClick = {
                    if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                        UserApiClient.instance.loginWithKakaoTalk(
                            context = context,
                            callback = kakaoLoginCallback
                        )
                    } else {
                        UserApiClient.instance.loginWithKakaoAccount(
                            context = context,
                            callback = kakaoLoginCallback
                        )
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.8f)
            )
        }
    }

    LaunchedEffect(uiState.loginResult) {
        when (val result = uiState.loginResult) {
            is ResultState.Success -> {
                navController.navigate("mainScreen") {
                    popUpTo("loginScreen") { inclusive = true }
                }
            }
            is ResultState.Error -> {
                Toast.makeText(context, context.getString(result.messageRes), Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }
}
