package com.swallaby.foodon.domain.main.model

data class NutrientIntake(
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
