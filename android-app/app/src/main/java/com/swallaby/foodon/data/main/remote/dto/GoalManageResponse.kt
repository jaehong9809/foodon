package com.swallaby.foodon.data.main.remote.dto

import com.swallaby.foodon.domain.main.model.GoalManage

data class GoalManageResponse(
    val managementTypeDescription: String,
    val targetCalories: Int,
    val carbRatio: Int,
    val proteinRatio: Int,
    val fatRatio: Int,
    val height: Int,
    val currentWeight: Int,
    val goalWeight: Int
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