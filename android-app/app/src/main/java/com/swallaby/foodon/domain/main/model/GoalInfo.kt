package com.swallaby.foodon.domain.main.model

import com.swallaby.foodon.presentation.navigation.NavRoutes

data class GoalInfo(
    val title: String = "",
    val content: String = "",
    val route: NavRoutes
)
