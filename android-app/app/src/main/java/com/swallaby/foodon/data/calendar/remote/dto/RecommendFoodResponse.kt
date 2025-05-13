package com.swallaby.foodon.data.calendar.remote.dto

import com.swallaby.foodon.domain.calendar.model.Effect
import com.swallaby.foodon.domain.calendar.model.RecommendFood

data class RecommendFoodResponse(
    val foodId: Long = 0,
    val foodName: String = "",
    val kcal: Int = 0,
    val reason: String = "",
    val effects: List<Effect> = emptyList()
)

fun RecommendFoodResponse.toDomain(): RecommendFood {
    return RecommendFood(
        foodId = this.foodId,
        name = this.foodName,
        kcal = this.kcal,
        reason = this.reason,
        effects = this.effects
    )
}
