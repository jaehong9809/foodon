package com.swallaby.foodon.domain.calendar.model

import com.swallaby.foodon.domain.food.model.NutrientClaimInfo

data class RecommendFood(
    val foodId: Long = 0,
    val name: String = "",
    val kcal: Double = 0.0,
    val nutrientClaims: List<NutrientClaimInfo> = emptyList()
)
