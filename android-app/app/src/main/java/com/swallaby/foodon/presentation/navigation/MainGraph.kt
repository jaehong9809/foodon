package com.swallaby.foodon.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.swallaby.foodon.presentation.main.MainScreen
import com.swallaby.foodon.presentation.main.NutrientDetailScreen

fun NavGraphBuilder.mainGraph(
    navController: NavHostController
) {

    navigation(
        startDestination = NavRoutes.Main.route, route = NavRoutes.MainGraph.route
    ) {
        composable(NavRoutes.Main.route) {
            MainScreen()
        }

        composable(NavRoutes.NutrientDetail.route) {
            NutrientDetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }

}