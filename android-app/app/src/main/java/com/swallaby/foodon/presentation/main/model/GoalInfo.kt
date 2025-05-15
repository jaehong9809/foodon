package com.swallaby.foodon.presentation.main.model

import com.swallaby.foodon.presentation.navigation.NavRoutes

data class GoalInfo(
    val title: String = "",
    val content: String = "",
    val route: NavRoutes
)
