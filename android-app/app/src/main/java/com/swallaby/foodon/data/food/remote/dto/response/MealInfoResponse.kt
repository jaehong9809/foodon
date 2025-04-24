package com.swallaby.foodon.data.food.remote.dto.response

import com.swallaby.foodon.domain.food.model.MealInfo
import com.swallaby.foodon.domain.food.model.MealNutrientWithPosition

data class MealInfoResponse(
    val imageUrl: String,
    val totalCarbs: Int,
    val totalFat: Int,
    val totalKcal: Int,
    val totalProtein: Int,
    val mealItems: List<MealNutrientWithPosition>,
)

fun MealInfoResponse.toDomain(): MealInfo = MealInfo(
    imageUrl = imageUrl,
    totalCarbs = totalCarbs,
    totalFat = totalFat,
    totalKcal = totalKcal,
    totalProtein = totalProtein,
    mealItems = mealItems
)