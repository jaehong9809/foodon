package com.swallaby.foodon.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swallaby.foodon.R
import com.swallaby.foodon.core.ui.theme.WB500
import com.swallaby.foodon.presentation.navigation.NavRoutes


@Composable
fun SplashScreen(
    onNavigate : (NavRoutes) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = WB500),
        contentAlignment = Alignment.Center
    ) {
        // TODO: 추후 권한 얻으면 로고에 JSON 애니메이션 추가

        Image(
            painter = painterResource(id = R.drawable.logo_splash),
            contentDescription = "foodOn Logo",
            modifier = Modifier.size(188.dp)
        )
    }
}