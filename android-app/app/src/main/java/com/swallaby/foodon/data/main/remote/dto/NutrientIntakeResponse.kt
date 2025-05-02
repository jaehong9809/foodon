package com.swallaby.foodon.data.main.remote.dto

import com.swallaby.foodon.domain.main.model.NutrientIntake

data class NutrientIntakeResponse(
    val intakeKcal: Int = 0,
    val goalKcal: Int = 0,
    val intakeCarbs: Int = 0,
    val targetCarbs: Int = 0,
    val intakeProtein: Int = 0,
    val targetProtein: Int = 0,
    val intakeFat: Int = 0,
    val targetFat: Int = 0
)

fun NutrientIntakeResponse.toDomain(): NutrientIntake {
    return NutrientIntake(
        intakeKcal = this.intakeKcal,
        goalKcal = this.goalKcal,
        intakeCarbs = this.intakeCarbs,
        targetCarbs = this.targetCarbs,
        intakeProtein = this.intakeProtein,
        targetProtein = this.targetProtein,
        intakeFat = this.intakeFat,
        targetFat = this.targetFat
    )
}
