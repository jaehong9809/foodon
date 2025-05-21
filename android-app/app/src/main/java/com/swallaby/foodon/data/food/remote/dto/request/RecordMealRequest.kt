package com.swallaby.foodon.data.food.remote.dto.request

import com.swallaby.foodon.domain.food.model.MealItem
import com.swallaby.foodon.domain.food.model.MealType

data class RecordMealRequest(
    val imageFileName: String = "",
    val totalKcal: Int = 0,
    val totalCarbs: Double = 0.0,
    val totalProtein: Double = 0.0,
    val totalFat: Double = 0.0,
    val mealTimeType: MealType = MealType.BREAKFAST,
    val mealTime: String = "",
    val mealItems: List<MealItem> = emptyList(),
)


