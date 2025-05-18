package com.swallaby.foodon.domain.food.model

data class Food(
    val id: Long,
    val name: String,
    val servingUnit: String,
    val kcal: Int,
    val isCustom: Boolean = false
)
