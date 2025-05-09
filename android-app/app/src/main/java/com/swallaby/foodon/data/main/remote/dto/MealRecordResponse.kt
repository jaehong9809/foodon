package com.swallaby.foodon.data.main.remote.dto

import com.swallaby.foodon.domain.food.model.MealType
import com.swallaby.foodon.domain.main.model.MealRecord

data class MealRecordResponse(
    val mealId: Long = 0,
    val mealTimeType: MealType = MealType.BREAKFAST,
    val mealTime: String = "",
    val imageUrl: String = "",
    val mealItems: List<String> = emptyList(),
    val totalKcal: Int = 0,
    val totalCarbs: Double = 0.0,
    val totalProtein: Double = 0.0,
    val totalFat: Double = 0.0,
)

fun MealRecordResponse.toDomain(): MealRecord {
    return MealRecord(
        mealId = this.mealId,
        mealTimeType = this.mealTimeType,
        mealTime = this.mealTime,
        imageUrl = this.imageUrl,
        mealItems = this.mealItems,
        totalKcal = this.totalKcal,
        totalCarbs = this.totalCarbs,
        totalProtein = this.totalProtein,
        totalFat = this.totalFat
    )
}