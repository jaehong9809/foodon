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
        object MealDetail : NavRoutes("food_detail")

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

    /* SignUp */
    object SignUpGraph : NavRoutes("sign_up_graph")
    object SignUpGender : NavRoutes("sign_up_gender")
    object SignUpManagement : NavRoutes("sign_up_management")
    object SignUpActivity : NavRoutes("sign_up_activity")
    object SignUpBodyInfo : NavRoutes("sign_up_body_info")
    object SignUpGoalWeight : NavRoutes("sign_up_goal_weight")

}