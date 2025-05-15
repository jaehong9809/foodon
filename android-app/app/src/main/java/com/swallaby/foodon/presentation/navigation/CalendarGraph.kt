package com.swallaby.foodon.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.swallaby.foodon.presentation.calendar.CalendarScreen
import com.swallaby.foodon.presentation.calendar.CurrentWeightScreen
import com.swallaby.foodon.presentation.calendar.viewmodel.CalendarViewModel

fun NavGraphBuilder.calendarGraph(
    navController: NavHostController,
    calendarViewModel: CalendarViewModel
) {
    navigation(
        startDestination = NavRoutes.Calendar.route, route = NavRoutes.CalendarGraph.route
    ) {
        composable(
            route = NavRoutes.Calendar.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(500)
                )
            }
        ) {
            CalendarScreen(calendarViewModel)
        }

        composable(
            route = NavRoutes.CurrentWeight.route,
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
            CurrentWeightScreen(
                onBack = { navController.popBackStack() },
                onSubmit = { weight ->
                    calendarViewModel.updateUserWeight(weight)
                    navController.popBackStack()
                },
                viewModel = calendarViewModel
            )
        }
    }

}