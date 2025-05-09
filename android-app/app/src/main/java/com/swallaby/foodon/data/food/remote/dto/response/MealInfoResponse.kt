package com.swallaby.foodon.data.food.remote.dto.response

import com.swallaby.foodon.domain.food.model.MealInfo
import com.swallaby.foodon.domain.food.model.MealItem

data class MealInfoResponse(
    val imageUrl: String = "",
    val mealTime: String = "",
    val mealTimeType: String = "",
    val totalCarbs: Int = 0,
    val totalFat: Int = 0,
    val totalKcal: Int = 0,
    val totalProtein: Int = 0,
    val mealItems: List<MealItem>,
)

fun MealInfoResponse.toDomain(): MealInfo = MealInfo(
    imageUrl = imageUrl,
    mealTime = mealTime,
    mealTimeType = mealTimeType,
    totalCarbs = totalCarbs,
    totalFat = totalFat,
    totalKcal = totalKcal,
    totalProtein = totalProtein,
    mealItems = mealItems
)