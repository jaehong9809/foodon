package com.swallaby.foodon.domain.calendar.model

data class RecommendFood(
    val foodRecommendId: Long = 0,
    val foodName: String = "",
    val kcal: Int = 0,
    val reason: String = "",
    val effects: List<Effect> = emptyList()
)
