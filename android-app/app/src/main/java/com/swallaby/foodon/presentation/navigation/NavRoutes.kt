package com.swallaby.foodon.presentation.navigation

sealed class NavRoutes(val route: String) {

    /* Main */
    object MainGraph : NavRoutes("main_graph")
    object Main : NavRoutes("main")
    object NutrientDetail : NavRoutes("nutrient_detail")

    /* Calendar */
    object CalendarGraph : NavRoutes("calendar_graph")
    object Calendar : NavRoutes("calendar")

}