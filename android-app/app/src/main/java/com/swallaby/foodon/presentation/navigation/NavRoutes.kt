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
        object MealDetail : NavRoutes("meal_detail/{mealId}") {
            const val MEAL_ID = "mealId"
            fun createRoute(mealId: Long) = "meal_detail/$mealId"
        }

        object FoodEdit : NavRoutes("food_edit/{foodId}") {
            const val FOOD_ID = "foodId"
            fun createRoute(foodId: Long) = "food_edit/$foodId"
        }

        object FoodNutritionEdit : NavRoutes("food_nutrition_edit/{foodId}") {
            const val FOOD_ID = "foodId"
            fun createRoute(foodId: Long) = "food_nutrition_edit/$foodId"
        }


        object FoodRecord : NavRoutes("food_record")
    }

    /* Login */
    object LoginGraph : NavRoutes("login_graph")
    object Login : NavRoutes("login")

}