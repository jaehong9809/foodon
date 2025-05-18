package com.swallaby.foodon.data.food.local.dto

import androidx.room.ColumnInfo

data class LocalFoodDto(
    val id: Long,
    val foodId: Long,
    val name: String,
    val servingUnit: String,
    val kcal: Int,
    val isCustom: Boolean,
    @ColumnInfo(name = "prefix_match")
    val prefixMatch: Int
)
