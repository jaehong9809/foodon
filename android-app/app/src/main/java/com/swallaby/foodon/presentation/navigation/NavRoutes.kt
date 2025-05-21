package com.swallaby.foodon.presentation.navigation

sealed class NavRoutes(val route: String) {

    /* Main */
    object MainGraph : NavRoutes("main_graph")
    object Main : NavRoutes("main")
    object NutrientDetail : NavRoutes("nutrient_detail")
    object MainManagementType : NavRoutes("main_management_type")
    object MainBodyInfo : NavRoutes("main_body_info")
    object MainGoalWeight : NavRoutes("main_goal_weight")

    /* Calendar */
    object CalendarGraph : NavRoutes("calendar_graph")
    object Calendar : NavRoutes("calendar")
    object CurrentWeight : NavRoutes("update_current_weight")

    /* Food */
    object FoodGraph : NavRoutes("food_graph") {
        object MealDetail : NavRoutes("meal_detail/{mealId}") {
            const val MEAL_ID = "mealId"
            fun createRoute(mealId: Long) = "meal_detail/$mealId"
        }

        object FoodEdit : NavRoutes("meal_detail/{mealId}/food_edit/{foodId}") {
            const val FOOD_ID = "foodId"
            const val MEAL_ID = "mealId"
            fun createRoute(mealId: Long, foodId: Long) = "meal_detail/$mealId/food_edit/$foodId"
        }

        object FoodNutritionEdit : NavRoutes("meal_detail/{mealId}/food_nutrition_edit/{foodId}") {
            const val FOOD_ID = "foodId"
            const val MEAL_ID = "mealId"
            fun createRoute(mealId: Long, foodId: Long) =
                "meal_detail/$mealId/food_nutrition_edit/$foodId"
        }


        object FoodRecord : NavRoutes("food_record")

        object FoodRegister :
            NavRoutes("food_register?foodId={foodId}&mealId={mealId}&fromRecord={fromRecord}") {
            const val FOOD_ID = "foodId"
            const val MEAL_ID = "mealId"
            const val FROM_RECORD = "fromRecord"

            fun createRoute(mealId: Long?, foodId: Long?, fromRecord: Boolean = false): String {
                return if (mealId != null && foodId != null) {
                    "food_register?foodId=$foodId&mealId=$mealId"
                } else {
                    "food_register?fromRecord=$fromRecord"
                }
            }
        }

        object FoodSearch :
            NavRoutes("food_search?foodId={foodId}&mealId={mealId}&fromRecord={fromRecord}") {
            const val FOOD_ID = "foodId"
            const val MEAL_ID = "mealId"
            const val FROM_RECORD = "fromRecord"

            fun createRoute(mealId: Long?, foodId: Long?, fromRecord: Boolean = false): String {
                return if (mealId != null && foodId != null) {
                    "food_search?foodId=$foodId&mealId=$mealId"
                } else {
                    "food_search?fromRecord=$fromRecord"
                }
            }
        }
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

    /* Splash */
    object SplashGraph : NavRoutes("splash_graph")
    object Splash : NavRoutes("splash")

}