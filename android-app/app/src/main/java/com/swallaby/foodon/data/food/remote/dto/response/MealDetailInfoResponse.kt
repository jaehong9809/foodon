package com.swallaby.foodon.data.food.remote.dto.response

import com.swallaby.foodon.domain.food.model.MealInfo
import com.swallaby.foodon.domain.food.model.MealItem
import com.swallaby.foodon.domain.food.model.MealType
import org.threeten.bp.LocalDateTime
data class MealDetailInfoResponse(
    val mealId: Long = 0,
    val mealDateTime: String = "",// LocalDateTime.now(),
    val mealTimeType: MealType = MealType.BREAKFAST,
    val imageFileName: String = "",
    val totalCarbs: Double = 0.0,
    val totalFat: Double = 0.0,
    val totalKcal: Double = 0.0,
    val totalProtein: Double = 0.0,
    val mealItems: List<MealItem> = emptyList(),
)

fun MealDetailInfoResponse.toDomain(): MealInfo = MealInfo(
    mealId = mealId,
    mealTime = mealDateTime,
    mealTimeType = mealTimeType,
    imageFileName = imageFileName,
    totalCarbs = totalCarbs,
    totalFat = totalFat,
    totalKcal = totalKcal.toInt(),
    totalProtein = totalProtein,
    mealItems = mealItems
)