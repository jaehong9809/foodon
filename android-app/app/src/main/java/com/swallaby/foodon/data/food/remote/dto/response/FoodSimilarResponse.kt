package com.swallaby.foodon.data.food.remote.dto.response

import com.swallaby.foodon.domain.food.model.FoodSimilar

data class FoodSimilarResponse(
    val foodId: Long,
    val foodName: String,
)

fun FoodSimilarResponse.toDomain(): FoodSimilar = FoodSimilar(
    foodId = foodId,
    foodName = foodName,
)
