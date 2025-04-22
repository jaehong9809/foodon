package com.swallaby.foodon.presentation.navigation

sealed class NavRoutes(val route: String) {

    /* Main */
    object Main : NavRoutes("main")
    object NutrientDetail : NavRoutes("nutrient_detail")

    /* Calendar */
    object Calendar : NavRoutes("calendar")

}