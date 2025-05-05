package com.swallaby.foodon.domain.food.model

data class Nutrition(
    val nutritionType: NutritionType = NutritionType.FAT,
    val amount: Int = 0,
    val ratio: Float = 0f,
)

