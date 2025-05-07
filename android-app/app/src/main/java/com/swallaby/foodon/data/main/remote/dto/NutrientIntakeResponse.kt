package com.swallaby.foodon.data.main.remote.dto

import com.swallaby.foodon.domain.main.model.NutrientIntake

data class NutrientIntakeResponse(
    val intakeKcal: Int = 0,
    val goalKcal: Int = 0,
    val intakeCarbs: Int = 0,
    val goalCarbs: Int = 0,
    val intakeProtein: Int = 0,
    val goalProtein: Int = 0,
    val intakeFat: Int = 0,
    val goalFat: Int = 0
)

fun NutrientIntakeResponse.toDomain(): NutrientIntake {
    return NutrientIntake(
        intakeKcal = this.intakeKcal,
        goalKcal = this.goalKcal,
        intakeCarbs = this.intakeCarbs,
        goalCarbs = this.goalCarbs,
        intakeProtein = this.intakeProtein,
        goalProtein = this.goalProtein,
        intakeFat = this.intakeFat,
        goalFat = this.goalFat
    )
}
