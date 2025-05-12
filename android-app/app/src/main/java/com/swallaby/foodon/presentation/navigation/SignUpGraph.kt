package com.swallaby.foodon.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.swallaby.foodon.presentation.signup.ActivityTypeScreen
import com.swallaby.foodon.presentation.signup.GenderScreen
import com.swallaby.foodon.presentation.signup.GoalWeightScreen
import com.swallaby.foodon.presentation.signup.ManagementTypeScreen
import com.swallaby.foodon.presentation.signup.WeightScreen
import com.swallaby.foodon.presentation.signup.viewmodel.SignUpViewModel

fun NavGraphBuilder.signUpGraph(
    navController: NavHostController,
    signUpViewModel: SignUpViewModel
) {
    navigation(
        startDestination = NavRoutes.SignUpGender.route,
        route = NavRoutes.SignUpGraph.route
    ) {
        composable(NavRoutes.SignUpGender.route) {
            GenderScreen(
                onBack = { /* 첫 번째 화면, 뒤로 가기 불가능 */ },
                onNext = { navController.navigate(NavRoutes.SignUpManagement.route) },
                viewModel = signUpViewModel
            )

        }
        composable(NavRoutes.SignUpManagement.route) {
            ManagementTypeScreen(
                onNext = { navController.navigate(NavRoutes.SignUpActivity.route) },
                viewModel = signUpViewModel
            )
        }
        composable(NavRoutes.SignUpActivity.route) {
            ActivityTypeScreen(
                onNext = { navController.navigate(NavRoutes.SignUpWeight.route) },
                viewModel = signUpViewModel
            )
        }
        composable(NavRoutes.SignUpWeight.route) {
            WeightScreen(
                onNext = { navController.navigate(NavRoutes.SignUpGoalWeight.route) },
                viewModel = signUpViewModel
            )
        }
        composable(NavRoutes.SignUpGoalWeight.route) {
            GoalWeightScreen(
                onSubmit = {
                    //signUpViewModel.submitProfile()
                    navController.navigate(NavRoutes.MainGraph.route) {
                        popUpTo(NavRoutes.SignUpGraph.route) { inclusive = true }
                    }
                },
                viewModel = signUpViewModel
            )
        }
    }
}
