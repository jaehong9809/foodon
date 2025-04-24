package com.swallaby.foodon.data.food.remote.dto.request

import com.swallaby.foodon.domain.food.model.MealNutrientWithPosition

data class MealInfoRequest(
    val imageUrl: String,
    val totalCarbs: Int,
    val totalFat: Int,
    val totalKcal: Int,
    val totalProtein: Int,
    // todo mealItems 변경
    val mealItems: List<MealNutrientWithPosition>,
)
