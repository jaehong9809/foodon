package com.swallaby.foodon.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.swallaby.foodon.presentation.main.MainScreen
import com.swallaby.foodon.presentation.main.NutrientDetailScreen
import com.swallaby.foodon.presentation.main.viewmodel.MainViewModel

fun NavGraphBuilder.mainGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {

    navigation(
        startDestination = NavRoutes.Main.route, route = NavRoutes.MainGraph.route
    ) {
        composable(NavRoutes.Main.route) {
            MainScreen(mainViewModel)
        }

        composable(NavRoutes.NutrientDetail.route) {
            NutrientDetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }

}