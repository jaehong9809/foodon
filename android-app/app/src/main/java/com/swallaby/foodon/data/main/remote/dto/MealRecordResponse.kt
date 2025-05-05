package com.swallaby.foodon.data.main.remote.dto

import com.swallaby.foodon.domain.main.model.MealRecord
import com.swallaby.foodon.domain.main.model.MealTimeType

data class MealRecordResponse(
    val mealId: Long = 0,
    val mealTimeType: MealTimeType = MealTimeType.BREAKFAST,
    val mealTime: String = "",
    val imageUrl: String = "",
    val mealItems: List<String> = emptyList(),
    val totalKcal: Int = 0,
    val totalCarbs: Int = 0,
    val totalProtein: Int = 0,
    val totalFat: Int = 0
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