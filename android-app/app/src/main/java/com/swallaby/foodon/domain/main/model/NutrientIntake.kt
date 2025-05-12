package com.swallaby.foodon.domain.main.model

data class NutrientIntake(
    val intakeLogId: Long = 0,
    val date: String = "",
    val intakeKcal: Int = 0,
    val goalKcal: Int = 0,
    val intakeCarbs: Int = 0,
    val goalCarbs: Int = 0,
    val intakeProtein: Int = 0,
    val goalProtein: Int = 0,
    val intakeFat: Int = 0,
    val goalFat: Int = 0
)
