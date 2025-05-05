package com.swallaby.foodon.data.food.remote.dto.request

import com.swallaby.foodon.domain.food.model.MealItem
import com.swallaby.foodon.domain.food.model.MealType

data class MealInfoRequest(
    val imageUrl: String,
    val totalCarbs: Int,
    val totalFat: Int,
    val totalKcal: Int,
    val totalProtein: Int,
    val mealTimeType: MealType,
    val mealTime: String,
    // todo mealItems 변경
    val mealItems: List<MealItem>,
)
