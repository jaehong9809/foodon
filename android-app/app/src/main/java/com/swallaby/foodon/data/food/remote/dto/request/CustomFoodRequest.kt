package com.swallaby.foodon.data.food.remote.dto.request

import com.swallaby.foodon.domain.food.model.NutrientInfo
import com.swallaby.foodon.domain.food.model.UnitType

data class CustomFoodRequest(
    val foodName: String,
    val nutrients: NutrientInfo,
    val servingSize: Int,
    val unit: UnitType,
)

