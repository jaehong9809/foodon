package com.swallaby.foodon.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.swallaby.foodon.presentation.splash.SplashScreen

fun NavGraphBuilder.splashGraph(
    navController: NavHostController
) {
    composable(route = NavRoutes.Splash.route) {
        SplashScreen(
            onNavigate = { destination ->
                navController.navigate(destination.route) {
                    popUpTo(NavRoutes.Splash.route) { inclusive = true }
                }
            }
        )
    }
}
