package com.swallaby.foodon.presentation.navigation

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
        composable(NavRoutes.Calendar.route) {
            calendarViewModel.selectTab(0)
            CalendarScreen(calendarViewModel)
        }
    }

}