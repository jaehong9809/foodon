package com.swallaby.foodon.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.swallaby.foodon.presentation.foodedit.FoodEditScreen
import com.swallaby.foodon.presentation.mealdetail.MealDetailScreen
import com.swallaby.foodon.presentation.mealdetail.viewmodel.MealEditViewModel

fun NavGraphBuilder.mealGraph(navController: NavHostController) {
    navigation(
        startDestination = NavRoutes.FoodGraph.FoodRecord.route, route = NavRoutes.FoodGraph.route
    ) {
        composable(NavRoutes.FoodGraph.FoodRecord.route) {
            // TODO: 식사 기록 카메라 화면
        }

        composable(
            route = NavRoutes.FoodGraph.MealDetail.route,
        ) {
            val mealEditViewModel = hiltViewModel<MealEditViewModel>()
            MealDetailScreen(
                viewModel = mealEditViewModel,
                onBackClick = { navController.popBackStack() },
                onFoodClick = {
                    navController.navigate(NavRoutes.FoodGraph.FoodEdit.createRoute(it))
                },
            )
        }

        composable(NavRoutes.FoodGraph.FoodEdit.route) {
            val foodId = it.arguments?.getLong("foodId") ?: 0
            FoodEditScreen(onBackClick = { navController.popBackStack() }, foodId = foodId)
        }
    }
}
