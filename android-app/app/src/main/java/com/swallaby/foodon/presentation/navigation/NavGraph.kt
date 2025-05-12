package com.swallaby.foodon.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.swallaby.foodon.presentation.calendar.viewmodel.CalendarViewModel
import com.swallaby.foodon.presentation.main.viewmodel.MainViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    NavControllerProvider(navController = navController) {

        val mainViewModel: MainViewModel = hiltViewModel()
        val calendarViewModel: CalendarViewModel = hiltViewModel()

        NavHost(
            navController = navController,
            startDestination = NavRoutes.SplashGraph.route,
            modifier = modifier
        ) {
            splashGraph(navController)
            mainGraph(navController, mainViewModel)
            loginGraph(navController)
            calendarGraph(navController, calendarViewModel)
            mealGraph(navController)
        }
    }

}