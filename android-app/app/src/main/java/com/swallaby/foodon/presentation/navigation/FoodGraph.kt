package com.swallaby.foodon.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.swallaby.foodon.presentation.foodDetail.FoodDetailScreen

fun NavGraphBuilder.foodGraph(navController: NavHostController) {
    navigation(
        startDestination = NavRoutes.FoodGraph.FoodDetail.route,
        route = NavRoutes.FoodGraph.route
    ) {
        composable(NavRoutes.FoodGraph.FoodDetail.route) {
            FoodDetailScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
