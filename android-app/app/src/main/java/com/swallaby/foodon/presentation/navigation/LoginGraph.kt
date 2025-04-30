package com.swallaby.foodon.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.swallaby.foodon.presentation.login.LoginScreen

fun NavGraphBuilder.loginGraph(navController: NavController) {
    navigation(
        startDestination = NavRoutes.Login.route,
        route = NavRoutes.LoginGraph.route
    ) {
        composable(NavRoutes.Login.route) {
            LoginScreen()
        }
    }
}
