package com.swallaby.foodon.domain.food.model

data class MealItem(
    val type: String = "PUBLIC",
    val foodId: Long = 0,
    val foodName: String = "",
    val unit: String = "",
    val quantity: Int = 0,
    val nutrientInfo: NutrientInfo = NutrientInfo(),
    val positions: List<Position> = emptyList(),
)

