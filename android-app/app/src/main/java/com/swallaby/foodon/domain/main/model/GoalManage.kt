package com.swallaby.foodon.domain.main.model

data class GoalManage(
    val managementType: String = "",
    val targetCalories: Int = 0,
    val carbRatio: Int = 0,
    val proteinRatio: Int = 0,
    val fatRatio: Int = 0,
    val height: Int = 0,
    val currentWeight: Int = 0,
    val goalWeight: Int = 0
)