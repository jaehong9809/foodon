package com.swallaby.foodon.domain.food.model

import android.net.Uri

data class MealInfo(
    val imageUri: Uri? = null,
    val imageFileName: String = "",
    val mealTime: String = "",
    val mealTimeType: String = "",
    val totalCarbs: Int = 0,
    val totalFat: Int = 0,
    val totalKcal: Int = 0,
    val totalProtein: Int = 0,
    val mealItems: List<MealItem> = emptyList(),
)