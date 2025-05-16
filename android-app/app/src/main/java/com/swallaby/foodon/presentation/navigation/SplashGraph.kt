package com.swallaby.foodon.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.swallaby.foodon.presentation.splash.SplashScreen

fun NavGraphBuilder.splashGraph(navController: NavHostController) {
    navigation(
        startDestination = NavRoutes.Splash.route,
        route = NavRoutes.SplashGraph.route
    ) {
        composable(route = NavRoutes.Splash.route) {
            SplashScreen(
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        popUpTo(NavRoutes.SplashGraph.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
