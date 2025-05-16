package com.swallaby.foodon.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swallaby.foodon.R
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.data.auth.remote.result.AuthFlowResult
import com.swallaby.foodon.presentation.navigation.NavRoutes
import com.swallaby.foodon.presentation.splash.viewmodel.SplashViewModel


@Composable
fun SplashScreen(
    onNavigate : (NavRoutes) -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = WB500),
        contentAlignment = Alignment.Center
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(uiState.result) {
            when (val result = uiState.result) {
                is ResultState.Success -> {
                    when (result.data) {
                        AuthFlowResult.NavigateToMain -> onNavigate(NavRoutes.MainGraph)
                        AuthFlowResult.NavigateToSignUp -> onNavigate(NavRoutes.SignUpGraph)
                    }
                }
                is ResultState.Error -> {
                    onNavigate(NavRoutes.LoginGraph)
                }
                else -> Unit // Loading 상태일 때 대기, 추후 ProgressBar or Animation 추가 가능
            }
        }

        // TODO: 추후 권한 얻으면 로고에 JSON 애니메이션 추가
        Image(
            painter = painterResource(id = R.drawable.logo_splash),
            contentDescription = "foodOn Logo",
            modifier = Modifier.size(188.dp)
        )
    }
}