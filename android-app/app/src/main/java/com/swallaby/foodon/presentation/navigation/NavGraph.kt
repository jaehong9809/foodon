package com.swallaby.foodon.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavControllerProvider(navController = navController) {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Main.route,
            modifier = modifier
        ) {

            mainGraph(navController)

        }
    }

}