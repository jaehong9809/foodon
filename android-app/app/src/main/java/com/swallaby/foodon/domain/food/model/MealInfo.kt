package com.swallaby.foodon.domain.food.model

data class MealInfo(
    val imageUrl: String,
    val totalCarbs: Int,
    val totalFat: Int,
    val totalKcal: Int,
    val totalProtein: Int,
    val mealItems: List<MealNutrientWithPosition>,
)