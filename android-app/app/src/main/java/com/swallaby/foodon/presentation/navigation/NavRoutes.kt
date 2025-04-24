package com.swallaby.foodon.presentation.navigation

sealed class NavRoutes(val route: String) {

    /* Main */
    object MainGraph : NavRoutes("main_graph")
    object Main : NavRoutes("main")
    object NutrientDetail : NavRoutes("nutrient_detail")

    /* Calendar */
    object CalendarGraph : NavRoutes("calendar_graph")
    object Calendar : NavRoutes("calendar")

    /* Food */
    object FoodGraph : NavRoutes("food_graph") {
        object FoodDetail : NavRoutes("food_detail")
        object FoodEdit : NavRoutes("food_edit/{foodId}") {
            fun createRoute(foodId: Long) = "food_edit/$foodId"
        }
    }


}