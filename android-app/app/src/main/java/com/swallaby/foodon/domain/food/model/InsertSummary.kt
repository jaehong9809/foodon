package com.swallaby.foodon.domain.food.model

data class InsertSummary(
    val successCount: Int,
    val failureCount: Int,
    val failedFoods: List<Food>
)
