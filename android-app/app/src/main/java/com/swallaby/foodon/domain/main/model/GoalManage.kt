package com.swallaby.foodon.domain.main.model

data class GoalManage(
    val managementType: String,
    val targetCalories: Int,
    val carbRatio: Int,
    val proteinRatio: Int,
    val fatRatio: Int,
    val height: Int,
    val currentWeight: Int,
    val goalWeight: Int
)