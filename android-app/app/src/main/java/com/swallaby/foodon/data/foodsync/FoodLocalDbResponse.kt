package com.swallaby.foodon.data.foodsync

import com.swallaby.foodon.domain.food.model.Food

data class FoodLocalDbResponse(
    val foodId: Long,
    val foodName: String,
    val servingUnit: String,
    val kcal: Int,
    val isCustom: Boolean
)

fun FoodLocalDbResponse.toDomain(): Food {
    return Food(
        id = this.foodId,
        name = this.foodName,
        servingUnit = this.servingUnit,
        kcal = this.kcal,
        isCustom = this.isCustom
    )
}