package com.swallaby.foodon.domain.main.model

data class NutrientIntake(
    val intakeKcal: Int = 0,
    val goalKcal: Int = 0,
    val intakeCarbs: Int = 0,
    val targetCarbs: Int = 0,
    val intakeProtein: Int = 0,
    val targetProtein: Int = 0,
    val intakeFat: Int = 0,
    val targetFat: Int = 0
)
