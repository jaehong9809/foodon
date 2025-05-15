package com.swallaby.foodon.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.swallaby.foodon.presentation.calendar.CalendarScreen
import com.swallaby.foodon.presentation.calendar.viewmodel.CalendarViewModel

fun NavGraphBuilder.calendarGraph(
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
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(500)
                )
            }
        ) {
            calendarViewModel.selectTab(0)
            CalendarScreen(calendarViewModel)
        }
    }

}