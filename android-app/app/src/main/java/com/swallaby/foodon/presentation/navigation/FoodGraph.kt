package com.swallaby.foodon.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.swallaby.foodon.presentation.foodDetail.FoodDetailScreen
import com.swallaby.foodon.presentation.foodEdit.FoodEditScreen

fun NavGraphBuilder.foodGraph(navController: NavHostController) {
    navigation(
        startDestination = NavRoutes.FoodGraph.FoodDetail.route, route = NavRoutes.FoodGraph.route
    ) {
        composable(NavRoutes.FoodGraph.FoodDetail.route) {
            FoodDetailScreen(onBackClick = { navController.popBackStack() }, onFoodClick = {
                navController.navigate(NavRoutes.FoodGraph.FoodEdit.createRoute(it))
            })
        }

        composable(NavRoutes.FoodGraph.FoodEdit.route) {
            val foodId = it.arguments?.getLong("foodId") ?: 0
            FoodEditScreen(onBackClick = { navController.popBackStack() }, foodId = foodId)
        }
    }
}
