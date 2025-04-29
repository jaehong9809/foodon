package com.swallaby.foodon.domain.main.model

data class MealRecord(
    val mealId: Long = 0,
    val mealTimeType: MealTimeType = MealTimeType.BREAKFAST,
    val mealTime: String = "",
    val mealImageUrl: String = "",
    val mealItems: List<String> = emptyList(),
    val totalKcal: Int = 0,
    val totalCarbs: Int = 0,
    val totalProtein: Int = 0,
    val totalFat: Int = 0
)