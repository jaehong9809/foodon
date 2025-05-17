package com.swallaby.foodon.core.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun StatusBarConfig(
    darkIcons: Boolean = true,
    statusBarColor: Color = Color.Transparent,
) {
    val systemUiController = rememberSystemUiController()

    DisposableEffect(systemUiController, darkIcons, statusBarColor) {
        // 상태바 색상 및 아이콘 색상 설정
        systemUiController.setStatusBarColor(
            color = statusBarColor, darkIcons = darkIcons
        )

        onDispose {
            systemUiController.setStatusBarColor(
                color = statusBarColor, darkIcons = true
            )
        }
    }
}
