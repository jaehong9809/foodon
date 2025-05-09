package com.swallaby.foodon.presentation.navigation

import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.swallaby.foodon.core.result.ResultState
import com.swallaby.foodon.presentation.foodedit.FoodEditScreen
import com.swallaby.foodon.presentation.foodedit.viewmodel.FoodEditViewModel
import com.swallaby.foodon.presentation.mealdetail.MealDetailScreen
import com.swallaby.foodon.presentation.mealdetail.viewmodel.MealEditViewModel
import com.swallaby.foodon.presentation.mealrecord.MealRecordScreen
import com.swallaby.foodon.presentation.mealrecord.viewmodel.MealRecordViewModel
import com.swallaby.foodon.presentation.nutritionedit.NutritionEditScreen

fun NavGraphBuilder.mealGraph(navController: NavHostController) {
    navigation(
        startDestination = NavRoutes.FoodGraph.FoodRecord.route, route = NavRoutes.FoodGraph.route
    ) {
        composable(NavRoutes.FoodGraph.FoodRecord.route, exitTransition = { ExitTransition.None }) {
            val recordViewModel = hiltViewModel<MealRecordViewModel>()
            val mealEditViewModel = hiltViewModel<MealEditViewModel>()

            MealRecordScreen(recordViewModel = recordViewModel,
                editViewModel = mealEditViewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onSearchClick = {
                    // TODO: navigate SearchScreen
                },
                onNavigateToMealDetail = {
                    recordViewModel.resetMealRecordState()
                    navController.navigate(NavRoutes.FoodGraph.MealDetail.route)
                })
        }

        composable(
            route = NavRoutes.FoodGraph.MealDetail.route,
        ) {
            val backStackEntry = remember(navController.currentBackStackEntry) {
                navController.getBackStackEntry(NavRoutes.FoodGraph.FoodRecord.route)
            }
            val mealEditViewModel: MealEditViewModel = hiltViewModel(backStackEntry)

            MealDetailScreen(
                viewModel = mealEditViewModel,
                onBackClick = { navController.popBackStack() },
                onFoodClick = {
                    navController.navigate(NavRoutes.FoodGraph.FoodEdit.createRoute(it))
                },
            )
        }

        composable(
            route = NavRoutes.FoodGraph.FoodEdit.route,
            arguments = listOf(navArgument(NavRoutes.FoodGraph.FoodEdit.FOOD_ID) {
                type = NavType.LongType
            })
        ) {
            val foodId = it.arguments?.getLong(NavRoutes.FoodGraph.FoodEdit.FOOD_ID) ?: 0L
            val backStackEntry = remember(navController.currentBackStackEntry) {
                navController.getBackStackEntry(NavRoutes.FoodGraph.FoodRecord.route)
            }

            val mealEditViewModel: MealEditViewModel = hiltViewModel(backStackEntry)
            val mealEditUiState by mealEditViewModel.uiState.collectAsStateWithLifecycle()
            val mealInfo = (mealEditUiState.mealEditState as ResultState.Success).data

            // ViewModel 생성과 동시에 초기화
            val foodEditViewModel = hiltViewModel<FoodEditViewModel>().apply {
                initFood(mealInfo)
            }

            FoodEditScreen(
                viewModel = foodEditViewModel,
                onBackClick = { navController.popBackStack() },
                foodId = foodId,
                onNutritionEditClick = {
                    navController.navigate(NavRoutes.FoodGraph.FoodNutritionEdit.createRoute(foodId))
                },
                onFoodDeleteClick = {
                    mealEditViewModel.deleteFood(foodId)
                    navController.popBackStack()
                },
                onFoodUpdateClick = {
                    val updateMealInfo =
                        (foodEditViewModel.uiState.value.foodEditState as ResultState.Success).data
                    val food = updateMealInfo.mealItems.find { item -> item.foodId == foodId }

                    food?.let { it ->
                        mealEditViewModel.updateFood(it)
                        navController.popBackStack()
                    }
                },
            )
        }

        composable(
            NavRoutes.FoodGraph.FoodNutritionEdit.route,
            arguments = listOf(navArgument(NavRoutes.FoodGraph.FoodNutritionEdit.FOOD_ID) {
                type = NavType.LongType
            })
        ) {
            val foodId = it.arguments?.getLong(NavRoutes.FoodGraph.FoodNutritionEdit.FOOD_ID) ?: 0L
            val backStackEntry = remember(navController.currentBackStackEntry) {
                navController.getBackStackEntry(NavRoutes.FoodGraph.FoodEdit.createRoute(foodId = foodId))
            }
            val foodEditViewModel: FoodEditViewModel = hiltViewModel(backStackEntry)
            NutritionEditScreen(
                viewModel = foodEditViewModel,
                foodId = foodId,
                onBackClick = {
                    navController.popBackStack()
                },
                onFoodUpdateClick = { nutrientInfo ->
                    foodEditViewModel.updateFoodNutrients(foodId, nutrientInfo)
                    navController.popBackStack()
                },
            )
        }
    }
}
