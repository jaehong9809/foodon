package com.swallaby.foodon.data.main.remote.dto

import com.swallaby.foodon.domain.main.model.MealRecord
import com.swallaby.foodon.domain.main.model.MealTimeType

data class MealRecordResponse(
    val mealId: Long = 0,
    val mealTimeType: String = "",
    val mealTime: String = "",
    val mealImageUrl: String = "",
    val mealItems: List<String> = emptyList(),
    val totalKcal: Int = 0,
    val totalCarbs: Int = 0,
    val totalProtein: Int = 0,
    val totalFat: Int = 0
)

fun MealRecordResponse.toDomain(): MealRecord {
    return MealRecord(
        mealId = this.mealId,
        mealTimeType = MealTimeType.fromKorean(this.mealTimeType),
        mealTime = this.mealTime,
        mealImageUrl = this.mealImageUrl,
        mealItems = this.mealItems,
        totalKcal = this.totalKcal,
        totalCarbs = this.totalCarbs,
        totalProtein = this.totalProtein,
        totalFat = this.totalFat
    )
}