package com.swallaby.foodon.domain.main.model

import com.swallaby.foodon.domain.food.model.MealType

data class MealRecord(
    val mealId: Long = 0,
    val mealTimeType: MealType = MealType.BREAKFAST,
    val mealTime: String = "",
    val imageUrl: String = "",
    val mealItems: List<String> = emptyList(),
    val totalKcal: Int = 0,
    val totalCarbs: Double = 0.0,
    val totalProtein: Double = 0.0,
    val totalFat: Double = 0.0,
)