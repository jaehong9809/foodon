package com.swallaby.foodon.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavControllerProvider(navController = navController) {

    }

}