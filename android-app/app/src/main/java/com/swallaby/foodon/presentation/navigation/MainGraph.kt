package com.swallaby.foodon.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.swallaby.foodon.presentation.main.MainBodyInfoScreen
import com.swallaby.foodon.presentation.main.MainGoalWeightScreen
import com.swallaby.foodon.presentation.main.MainManagementTypeScreen
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

        composable(route = NavRoutes.Main.route) {
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
            enterTransition = slideLeftEnter(),
            popExitTransition = slideRightPopExit(),
        ) {
            NutrientDetailScreen(
                viewModel = mainViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.MainManagementType.route,
            enterTransition = slideLeftEnter(),
            popExitTransition = slideRightPopExit(),
        ) {
            MainManagementTypeScreen(
                viewModel = mainViewModel,
                onBack = { navController.popBackStack() },
                onSubmit = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.MainBodyInfo.route,
            enterTransition = slideLeftEnter(),
            popExitTransition = slideRightPopExit(),
        ) {
            MainBodyInfoScreen(
                viewModel = mainViewModel,
                onBack = { navController.popBackStack() },
                onSubmit = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.MainGoalWeight.route,
            enterTransition = slideLeftEnter(),
            popExitTransition = slideRightPopExit(),
        ) {
            MainGoalWeightScreen(
                viewModel = mainViewModel,
                onBack = { navController.popBackStack() },
                onSubmit = { navController.popBackStack() }
            )
        }
    }

}