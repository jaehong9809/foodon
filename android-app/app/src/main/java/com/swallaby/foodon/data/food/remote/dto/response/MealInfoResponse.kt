package com.swallaby.foodon.data.food.remote.dto.response

import com.swallaby.foodon.domain.food.model.MealInfo
import com.swallaby.foodon.domain.food.model.MealItem

data class MealInfoResponse(
     val imageFileName: String = "",
     val totalCarbs: Double = 0.0,
     val totalFat: Double = 0.0,
     val totalKcal: Double = 0.0,
     val totalProtein: Double = 0.0,
     val mealItems: List<MealItem> = emptyList(),
)

fun MealInfoResponse.toDomain(): MealInfo = MealInfo(
    imageFileName = imageFileName,
    totalCarbs = totalCarbs,
    totalFat = totalFat,
    totalKcal = totalKcal.toInt(),
    totalProtein = totalProtein,
    mealItems = mealItems
)