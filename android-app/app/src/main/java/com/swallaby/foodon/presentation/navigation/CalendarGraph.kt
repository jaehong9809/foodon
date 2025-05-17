package com.swallaby.foodon.presentation.navigation

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
            enterTransition = slideUpEnter(),
            exitTransition = slideLeftExit(),
            popEnterTransition = slideRightPopEnter(),
            popExitTransition = slideDownPopExit()
        ) {
            CalendarScreen(
                viewModel = calendarViewModel,
                onUpdateWeight = {
                    navController.navigate(NavRoutes.CurrentWeight.route)
                }
            )
        }

        composable(
            route = NavRoutes.CurrentWeight.route,
            enterTransition = slideLeftEnter(),
            popExitTransition = slideRightPopExit(),
        ) {
            CurrentWeightScreen(
                onBack = { navController.popBackStack() },
                onSubmit = {
                    navController.popBackStack()
                },
                viewModel = calendarViewModel
            )
        }
    }

}