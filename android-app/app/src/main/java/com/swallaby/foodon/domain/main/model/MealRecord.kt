package com.swallaby.foodon.domain.main.model

import com.swallaby.foodon.domain.food.model.MealType

data class MealRecord(
    val mealId: Long = 0,
    val mealTimeType: MealType = MealType.BREAKFAST,
    val mealTime: String = "",
    val imageUrl: String = "",
    val mealItems: List<String> = emptyList(),
    val totalKcal: Int = 0,
    val totalCarbs: Int = 0,
    val totalProtein: Int = 0,
    val totalFat: Int = 0
)