package com.swallaby.foodon.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.data.auth.remote.result.AuthFlowResult
import com.swallaby.foodon.presentation.navigation.NavRoutes
import com.swallaby.foodon.presentation.splash.viewmodel.SplashViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition


@Composable
fun SplashScreen(
    onNavigate : (NavRoutes) -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("SSAFY-Splash-Logo.json")
    )
    val lottieAnimState = animateLottieCompositionAsState(composition, iterations = 1)

    LaunchedEffect(lottieAnimState.isAtEnd) {
        if (lottieAnimState.isAtEnd) {
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
                else -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = WB500),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { lottieAnimState.progress },
            modifier = Modifier.size(188.dp)
        )
    }
}