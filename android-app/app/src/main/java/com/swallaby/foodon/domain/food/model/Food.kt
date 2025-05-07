package com.swallaby.foodon.domain.food.model

data class Food(
    val name: String,
    val servingUnit: String,
    val kcal: Int,
    val isRegistered: Boolean = false
)
