package com.swallaby.foodon.presentation.navigation

import android.util.Log
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
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
        composable(NavRoutes.FoodGraph.FoodRecord.route,
            popExitTransition = { ExitTransition.None },
            exitTransition = { ExitTransition.None }) {
            val recordViewModel = hiltViewModel<MealRecordViewModel>()
            MealRecordScreen(recordViewModel = recordViewModel,
                editViewModel = mealEditViewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onSearchClick = {
                    navController.navigate(
                        NavRoutes.FoodGraph.FoodSearch.createRoute(
                            null, null
                        )
                    )
                },
                onNavigateToMealDetail = {
                    recordViewModel.resetMealRecordState()
                    navController.navigate(NavRoutes.FoodGraph.MealDetail.createRoute(0))
                })
        }

        composable(
            route = NavRoutes.FoodGraph.MealDetail.route,
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
                },
                onNavigateSearch = {
                    navController.navigate(
                        NavRoutes.FoodGraph.FoodSearch.createRoute(
                            mealId, null
                        )
                    )
                })
        }

        composable(
            route = NavRoutes.FoodGraph.FoodEdit.route,
            arguments = listOf(navArgument(NavRoutes.FoodGraph.FoodEdit.FOOD_ID) {
                type = NavType.LongType
            }, navArgument(NavRoutes.FoodGraph.FoodEdit.MEAL_ID) {
                type = NavType.LongType
            }),
        ) {
            val foodId = it.arguments?.getLong(NavRoutes.FoodGraph.FoodEdit.FOOD_ID) ?: 0L
            val mealId = it.arguments?.getLong(NavRoutes.FoodGraph.FoodEdit.MEAL_ID) ?: 0L

            val mealEditUiState by mealEditViewModel.uiState.collectAsStateWithLifecycle()
            val mealInfo = (mealEditUiState.mealEditState as ResultState.Success).data

            // ViewModel 생성과 동시에 초기화
            val foodEditViewModel = hiltViewModel<FoodEditViewModel>().apply {
                initFood(mealInfo, foodId)
            }

            val foodEditUiState by foodEditViewModel.uiState.collectAsStateWithLifecycle()

            FoodEditScreen(mealId = mealId,
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
                    val selectedFoodId = foodEditViewModel.uiState.value.selectedFoodId
                    // 선택한 foodId 를 originalFoodId로 변경
                    val updatedItems = updateMealInfo.mealItems.map { food ->
                        if (food.foodId == selectedFoodId) {
                            food.copy(
                                originalFoodId = selectedFoodId
                            )
                        } else food
                    }

                    mealEditViewModel.updateMealItems(updatedItems)
                    navController.popBackStack()
                },
                onSearchClick = {
                    Log.d(
                        "FoodEditScreen",
                        "onSearchClick mealId = ${mealId}, foodId = $foodId, selectedFoodId = ${foodEditUiState.selectedFoodId}"
                    )
                    navController.navigate(
                        NavRoutes.FoodGraph.FoodSearch.createRoute(
                            mealId, foodId
                        )
                    )
                })
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
            val foodEditUiState by foodEditViewModel.uiState.collectAsStateWithLifecycle()
            NutritionEditScreen(viewModel = foodEditViewModel,
                foodId = foodEditUiState.selectedFoodId,
                onBackClick = {
                    navController.popBackStack()
                },
                onFoodUpdateClick = { mealItem ->
                    foodEditViewModel.updateCustomFood(mealItem)
                },
                onSuccessCustomFood = {
                    navController.popBackStack()
                })
        }

        composable(
            NavRoutes.FoodGraph.FoodRegister.route,
            arguments = listOf(navArgument(NavRoutes.FoodGraph.FoodRegister.MEAL_ID) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }, navArgument(NavRoutes.FoodGraph.FoodRegister.FOOD_ID) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) {
            val registerViewModel = hiltViewModel<FoodRegisterViewModel>()
            val mealId =
                it.arguments?.getString(NavRoutes.FoodGraph.FoodRegister.MEAL_ID)?.toLongOrNull()
                    ?: 0
            val foodId =
                it.arguments?.getString(NavRoutes.FoodGraph.FoodRegister.FOOD_ID)?.toLongOrNull()
            val backStackEntry = getFoodEditBackstackEntry(mealId, foodId, navController)
            val foodEditViewModel = backStackEntry?.let { backStack ->
                hiltViewModel<FoodEditViewModel>(backStack)
            }
            Log.d("FoodRegisterScreen", "foodEditViewModel = $foodEditViewModel")
            Log.d("FoodRegisterScreen", "mealId = $mealId, foodId = $foodId")

            FoodRegisterScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToSearch = { food -> // DB로 찾은 음식
                    // 음식을 수정 할 경우
                    foodEditViewModel?.let { viewModel ->
                        // 검색한 음식 영양소 조회 및 기존 음식과 교체하기 and 관련 검색어 조회
                        viewModel.fetchFood(food.foodId, FoodType.CUSTOM)
                        viewModel.fetchFoodSimilar(food.foodName)
                        Log.d(
                            "FoodRegisterScreen", "food = $food, foodId = $foodId, mealId = $mealId"
                        )
                        foodId?.let {
                            navController.navigate(
                                NavRoutes.FoodGraph.FoodEdit.createRoute(
                                    mealId = mealId, foodId = foodId
                                )
                            ) {
                                popUpTo(
                                    NavRoutes.FoodGraph.FoodSearch.createRoute(
                                        mealId = mealId, foodId = foodId
                                    )
                                ) { inclusive = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    } ?: run {
                        // 찾는 음식이 없어 새로 등록 할 경우
                        // todo 음식 기록일 경우 추가 - 상세 화면으로 라우팅
                        Log.d("FoodRegisterScreen", "register food")
                        mealEditViewModel.addFood(food.foodId, FoodType.CUSTOM)
                        navController.navigate(NavRoutes.FoodGraph.MealDetail.createRoute(0L)) {
                            popUpTo(NavRoutes.FoodGraph.MealDetail.createRoute(0L))
                            launchSingleTop = true
                            restoreState = true
                        }
                    }


                },
                viewModel = registerViewModel,
            )
        }

        composable(
            route = NavRoutes.FoodGraph.FoodSearch.route,
            arguments = listOf(navArgument(NavRoutes.FoodGraph.FoodSearch.MEAL_ID) {
                // LongType 으로 하면 primitive long 으로 돼서 null 값을 넘길 수 없음.
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }, navArgument(NavRoutes.FoodGraph.FoodSearch.FOOD_ID) {
                // LongType 으로 하면 primitive long 으로 돼서 null 값을 넘길 수 없음.
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { navBackStackEntry ->
            val foodId =
                navBackStackEntry.arguments?.getString(NavRoutes.FoodGraph.FoodSearch.FOOD_ID)
                    ?.toLongOrNull()
            val mealId =
                navBackStackEntry.arguments?.getString(NavRoutes.FoodGraph.FoodSearch.MEAL_ID)
                    ?.toLongOrNull() ?: 0L


            val backStackEntry = getFoodEditBackstackEntry(mealId, foodId, navController)
            Log.d("FoodSearchScreen", "backStackEntry = $backStackEntry")

            val foodEditViewModel = backStackEntry?.let {
                hiltViewModel<FoodEditViewModel>(it)
            }

            FoodSearchScreen(
                navController = navController,
                foodId = foodId,
                mealEditViewModel = mealEditViewModel,
                foodEditViewModel = foodEditViewModel,
            )
        }
    }
}

@Composable
private fun getFoodEditBackstackEntry(
    mealId: Long, foodId: Long?, navController: NavHostController,
): NavBackStackEntry? {
    val backStackEntry = if (mealId != null && foodId != null) {
        Log.d("FoodSearchScreen", "mealId: $mealId, foodId: $foodId")
        remember(navController.currentBackStackEntry) {
            navController.getBackStackEntry(
                NavRoutes.FoodGraph.FoodEdit.createRoute(
                    mealId = mealId,
                    foodId = foodId,
                )
            )
        }
    } else {
        null
    }
    return backStackEntry
}
