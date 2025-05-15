package com.swallaby.foodon.presentation.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.presentation.login.component.KakaoLoginButton
import com.swallaby.foodon.presentation.login.viewmodel.LoginViewModel
import com.swallaby.foodon.presentation.navigation.LocalNavController
import com.swallaby.foodon.presentation.navigation.NavRoutes
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.data.auth.remote.result.AuthFlowResult

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
                .background(WB500)
                .padding(paddingValues)
        ) {
            Image(
                painter = painterResource(R.drawable.logo_foodon),
                contentDescription = "FoodOn Logo",
                modifier = Modifier
                    .align(Alignment.Center)
                    .height(188.dp)
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 32.dp, vertical = 120.dp)
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
                        .fillMaxWidth()
                        .height(50.dp)
                )
            }
        }
    }

    LaunchedEffect(uiState.loginResult) {
        when (val result = uiState.loginResult) {
            is ResultState.Success -> {
                when (result.data) {
                    AuthFlowResult.NavigateToMain -> {
                        navController.navigate(NavRoutes.Main.route) {
                            popUpTo(NavRoutes.Login.route) { inclusive = true }
                        }
                    }
                    AuthFlowResult.NavigateToSignUp -> {
                        navController.navigate(NavRoutes.SignUpGraph.route) {
                            popUpTo(NavRoutes.Login.route) { inclusive = true }
                        }
                    }
                }
            }
            is ResultState.Error -> {
                Toast.makeText(context, context.getString(result.messageRes), Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

}
