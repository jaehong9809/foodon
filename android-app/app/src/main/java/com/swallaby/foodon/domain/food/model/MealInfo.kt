package com.swallaby.foodon.domain.food.model

import android.net.Uri
import com.swallaby.foodon.data.food.remote.dto.request.RecordMealRequest

data class MealInfo(
    val imageUri: Uri? = null,
    val imageFileName: String = "",
    val mealTime: String = "",
    val mealTimeType: MealType = MealType.BREAKFAST,
    val totalCarbs: Double = 0.0,
    val totalFat: Double = 0.0,
    val totalKcal: Int = 0,
    val totalProtein: Double = 0.0,
    val mealItems: List<MealItem> = emptyList(),
)

fun MealInfo.toRequest(): RecordMealRequest = RecordMealRequest(
    imageFileName = imageFileName,
    mealTime = mealTime,
    mealTimeType = mealTimeType,
    totalCarbs = totalCarbs,
    totalFat = totalFat,
    totalKcal = totalKcal,
    totalProtein = totalProtein,
    mealItems = mealItems
)
