package com.swallaby.foodon.domain.food.model

data class MealInfo(
    val imageUrl: String = "",
    val mealTime: String = "",
    val mealTimeType: String = "",
    val totalCarbs: Int = 0,
    val totalFat: Int = 0,
    val totalKcal: Int = 0,
    val totalProtein: Int = 0,
    val mealItems: List<MealItem>,
)