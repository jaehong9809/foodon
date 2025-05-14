package com.swallaby.foodon.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
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
            MainScreen(
                mainViewModel,
                onRecordClick = {
                    navController.navigate(NavRoutes.FoodGraph.FoodRecord.route)
                }
            )
        }

        composable(
            route = NavRoutes.NutrientDetail.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            NutrientDetailScreen(
                viewModel = mainViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }

}