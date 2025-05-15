package com.swallaby.foodon.presentation.navigation

import androidx.compose.animation.EnterTransition
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
import com.swallaby.foodon.domain.food.model.FoodType
import com.swallaby.foodon.presentation.foodedit.FoodEditScreen
import com.swallaby.foodon.presentation.foodedit.viewmodel.FoodEditViewModel
import com.swallaby.foodon.presentation.foodregister.FoodRegisterScreen
import com.swallaby.foodon.presentation.foodregister.viewmodel.FoodRegisterViewModel
import com.swallaby.foodon.presentation.foodsearch.FoodSearchScreen
import com.swallaby.foodon.presentation.mealdetail.MealDetailScreen
import com.swallaby.foodon.presentation.mealdetail.viewmodel.MealEditViewModel
import com.swallaby.foodon.presentation.mealrecord.MealRecordScreen
import com.swallaby.foodon.presentation.mealrecord.viewmodel.MealRecordViewModel
import com.swallaby.foodon.presentation.nutritionedit.NutritionEditScreen

fun NavGraphBuilder.mealGraph(
    navController: NavHostController,
    mealEditViewModel: MealEditViewModel,
) {
    navigation(
        startDestination = NavRoutes.FoodGraph.FoodRecord.route, route = NavRoutes.FoodGraph.route
    ) {
        composable(
            NavRoutes.FoodGraph.FoodRecord.route,
            exitTransition = { ExitTransition.None },
            enterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
        ) {
            val recordViewModel = hiltViewModel<MealRecordViewModel>()
            MealRecordScreen(recordViewModel = recordViewModel,
                editViewModel = mealEditViewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onSearchClick = {
                    navController.navigate(NavRoutes.FoodGraph.FoodSearch.route)
                },
                onNavigateToMealDetail = {
                    recordViewModel.resetMealRecordState()
                    navController.navigate(NavRoutes.FoodGraph.MealDetail.createRoute(0))
                })
        }

        composable(
            route = NavRoutes.FoodGraph.MealDetail.route,
            exitTransition = { ExitTransition.None },
            enterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            arguments = listOf(navArgument(NavRoutes.FoodGraph.MealDetail.MEAL_ID) {
                type = NavType.LongType
            })
        ) {
            val mealId = it.arguments?.getLong("mealId") ?: 0L

            MealDetailScreen(mealId = mealId,
                viewModel = mealEditViewModel,
                onBackClick = { navController.popBackStack() },
                onFoodClick = { foodId ->
                    navController.navigate(
                        NavRoutes.FoodGraph.FoodEdit.createRoute(
                            mealId = mealId, foodId = foodId
                        )
                    )
                },
                onNavigateMain = {
                    navController.popBackStack(NavRoutes.Main.route, inclusive = false)
                })
        }

        composable(
            route = NavRoutes.FoodGraph.FoodEdit.route,
            arguments = listOf(navArgument(NavRoutes.FoodGraph.FoodEdit.FOOD_ID) {
                type = NavType.LongType
            }, navArgument(NavRoutes.FoodGraph.FoodEdit.MEAL_ID) {
                type = NavType.LongType
            }),
            exitTransition = { ExitTransition.None },
            enterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
        ) {
            val foodId = it.arguments?.getLong(NavRoutes.FoodGraph.FoodEdit.FOOD_ID) ?: 0L
            val mealId = it.arguments?.getLong(NavRoutes.FoodGraph.FoodEdit.MEAL_ID) ?: 0L

            val mealEditUiState by mealEditViewModel.uiState.collectAsStateWithLifecycle()
            val mealInfo = (mealEditUiState.mealEditState as ResultState.Success).data

            // ViewModel 생성과 동시에 초기화
            val foodEditViewModel = hiltViewModel<FoodEditViewModel>().apply {
                initFood(mealInfo, foodId)
            }

            FoodEditScreen(
                mealId = mealId,
                viewModel = foodEditViewModel,
                onBackClick = { navController.popBackStack() },
                onNutritionEditClick = {
                    navController.navigate(
                        NavRoutes.FoodGraph.FoodNutritionEdit.createRoute(
                            mealId = mealId, foodId = foodId
                        )
                    )
                },
                onFoodDeleteClick = {
                    mealEditViewModel.deleteFood(it)
                    navController.popBackStack()
                },
                onFoodUpdateClick = {

                    val updateMealInfo =
                        (foodEditViewModel.uiState.value.foodEditState as ResultState.Success).data
                    val food =
                        updateMealInfo.mealItems.find { item -> item.foodId == foodEditViewModel.uiState.value.selectedFoodId }

                    food?.let { it ->
                        mealEditViewModel.updateFood(it.copy(type = FoodType.CUSTOM_MODIFIED))
                    }
                    navController.popBackStack()
                },
                onSearchClick = {
                    navController.navigate(NavRoutes.FoodGraph.FoodSearch.route)
                }
//                onSuccessCustomFood = { mealItem ->
//                    mealEditViewModel.updateFood(mealItem.copy(type = FoodType.CUSTOM))
//                }
            )
        }

        composable(
            NavRoutes.FoodGraph.FoodNutritionEdit.route,
            arguments = listOf(navArgument(NavRoutes.FoodGraph.FoodNutritionEdit.FOOD_ID) {
                type = NavType.LongType
            }, navArgument(NavRoutes.FoodGraph.FoodNutritionEdit.MEAL_ID) {
                type = NavType.LongType
            })
        ) {
            val foodId = it.arguments?.getLong(NavRoutes.FoodGraph.FoodNutritionEdit.FOOD_ID) ?: 0L
            val mealId = it.arguments?.getLong(NavRoutes.FoodGraph.FoodNutritionEdit.MEAL_ID) ?: 0L
            val backStackEntry = remember(navController.currentBackStackEntry) {
                navController.getBackStackEntry(
                    NavRoutes.FoodGraph.FoodEdit.createRoute(
                        mealId = mealId,
                        foodId = foodId,
                    )
                )
            }
            val foodEditViewModel: FoodEditViewModel = hiltViewModel(backStackEntry)
            NutritionEditScreen(viewModel = foodEditViewModel, foodId = foodId, onBackClick = {
                navController.popBackStack()
            }, onFoodUpdateClick = { mealItem ->
                foodEditViewModel.updateCustomFood(mealItem)
//                    foodEditViewModel.updateFoodNutrients(foodId, nutrientInfo)
//                    navController.popBackStack()
            }, onSuccessCustomFood = {
                navController.popBackStack()
            })
        }

        composable(
            NavRoutes.FoodGraph.FoodRegister.route,
        ) {
            val registerViewModel = hiltViewModel<FoodRegisterViewModel>()
            FoodRegisterScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                viewModel = registerViewModel,
            )
        }

        composable(
            NavRoutes.FoodGraph.FoodSearch.route,
        ) {
            FoodSearchScreen()
        }
    }
}
