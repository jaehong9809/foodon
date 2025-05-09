package com.swallaby.foodon.data.food.remote.dto.request

import com.swallaby.foodon.domain.food.model.NutrientInfo

data class CustomFoodRequest(
    val foodName: String,
    val nutrients: NutrientInfo,
    val servingSize: Int,
    val unit: String,
)
