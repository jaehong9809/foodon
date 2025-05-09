package com.swallaby.foodon.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.swallaby.foodon.presentation.debug.DebugControlScreen
import com.swallaby.foodon.presentation.debug.DebugViewModel

fun NavGraphBuilder.debugGraph(navController: NavHostController) {
    composable(NavRoutes.Debug.route) {
        val viewModel: DebugViewModel = hiltViewModel()
        DebugControlScreen(
            onInsertDummyData = { viewModel.insertDummyData() },
            onClearDb = { viewModel.clearDatabase() }
        )
    }
}