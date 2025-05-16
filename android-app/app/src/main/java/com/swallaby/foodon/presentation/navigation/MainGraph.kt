package com.swallaby.foodon.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.swallaby.foodon.presentation.main.MainScreen
import com.swallaby.foodon.presentation.main.NutrientDetailScreen
import com.swallaby.foodon.presentation.main.RootScreen
import com.swallaby.foodon.presentation.main.viewmodel.MainViewModel

fun NavGraphBuilder.mainGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {

    navigation(
//        startDestination = NavRoutes.Main.route, route = NavRoutes.MainGraph.route
        startDestination = "root", route = NavRoutes.MainGraph.route
    ) {

        // TODO: 테스트용. 삭제 예정
        composable("root") {
            RootScreen(navController, mainViewModel)
        }

        composable(NavRoutes.Main.route) {
            MainScreen(
                viewModel = mainViewModel,
                onMonthlyClick = {
                    navController.navigate(NavRoutes.CalendarGraph.route)
                },
                onRecordClick = {
                    navController.navigate(NavRoutes.FoodGraph.FoodRecord.route)
                },
                onMealClick = { mealId ->
                    navController.navigate(NavRoutes.FoodGraph.MealDetail.createRoute(mealId))
                },
                onClickNavigate = { navRoute ->
                    navController.navigate(navRoute.route)
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
            popExitTransition = {
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