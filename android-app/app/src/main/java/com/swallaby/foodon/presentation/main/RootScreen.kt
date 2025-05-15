package com.swallaby.foodon.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.swallaby.foodon.presentation.main.viewmodel.MainViewModel
import com.swallaby.foodon.presentation.navigation.NavRoutes

// TODO: 테스트용. 삭제 예정
@Composable
fun RootScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        navController.navigate("main") {
            popUpTo("root") { inclusive = true }
        }
    }
}
