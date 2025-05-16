package com.swallaby.foodon.data.main.remote.dto

import com.swallaby.foodon.domain.main.model.GoalManage

data class GoalManageResponse(
    val managementTypeDescription: String = "",
    val targetCalories: Int = 0,
    val carbRatio: Int = 0,
    val proteinRatio: Int = 0,
    val fatRatio: Int = 0,
    val height: Int = 0,
    val currentWeight: Int = 0,
    val goalWeight: Int = 0
)

fun GoalManageResponse.toDomain(): GoalManage {
    return GoalManage(
        managementType = this.managementTypeDescription,
        targetCalories = this.targetCalories,
        carbRatio = this.carbRatio,
        proteinRatio = this.proteinRatio,
        fatRatio = this.fatRatio,
        height = this.height,
        currentWeight = this.currentWeight,
        goalWeight = this.goalWeight
    )
}