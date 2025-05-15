package com.swallaby.foodon.data.calendar.remote.dto

import com.swallaby.foodon.domain.calendar.model.RecommendFood
import com.swallaby.foodon.domain.food.model.NutrientClaimInfo

data class RecommendFoodResponse(
    val foodId: Long = 0,
    val foodName: String = "",
    val kcal: Int = 0,
    val nutrientClaims: List<NutrientClaimInfo> = emptyList()
)

fun RecommendFoodResponse.toDomain(): RecommendFood {
    return RecommendFood(
        foodId = this.foodId,
        name = this.foodName,
        kcal = this.kcal,
        nutrientClaims = this.nutrientClaims
    )
}
