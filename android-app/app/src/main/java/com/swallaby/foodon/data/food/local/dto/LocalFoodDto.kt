package com.swallaby.foodon.data.food.local.dto

data class LocalFoodDto(
    val id: Long,
    val foodId: Long,
    val name: String,
    val servingUnit: String,
    val kcal: Int,
    val isCustom: Boolean,
    val prefixMatch: Int
)
