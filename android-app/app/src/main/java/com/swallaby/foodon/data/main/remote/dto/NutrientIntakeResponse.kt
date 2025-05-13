package com.swallaby.foodon.data.main.remote.dto

import com.swallaby.foodon.domain.main.model.NutrientIntake
import java.math.BigDecimal

data class NutrientIntakeResponse(
    val intakeLogId: Long = 0,
    val date: String = "",
    val intakeKcal: Int = 0,
    val goalKcal: Int = 0,
    val intakeCarbs: Double = 0.0,
    val goalCarbs: Double = 0.0,
    val intakeProtein: Double = 0.0,
    val goalProtein: Double = 0.0,
    val intakeFat: Double = 0.0,
    val goalFat: Double = 0.0
)

fun NutrientIntakeResponse.toDomain(): NutrientIntake {
    return NutrientIntake(
        intakeLogId = this.intakeLogId,
        date = this.date,
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
