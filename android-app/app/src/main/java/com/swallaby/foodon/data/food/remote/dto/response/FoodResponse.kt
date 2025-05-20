package com.swallaby.foodon.data.food.remote.dto.response

import com.swallaby.foodon.domain.food.model.FoodInfoWithId
import com.swallaby.foodon.domain.food.model.FoodType
import com.swallaby.foodon.domain.food.model.NutrientInfo
import com.swallaby.foodon.domain.food.model.UnitType

data class FoodResponse(
    val type: FoodType = FoodType.PUBLIC,
    val foodId: Long,
    val foodName: String = "",
    val unit: UnitType = UnitType.GRAM,
    val nutrientInfo: NutrientInfo = NutrientInfo(),
)

data class FoodWithServingSizeResponse(
    val type: FoodType = FoodType.PUBLIC,
    val foodId: Long,
    val foodName: String = "",
    val unit: UnitType = UnitType.GRAM,
    val servingSize : Double = 0.0,
    val nutrientInfo: NutrientInfo = NutrientInfo(),
)

fun FoodResponse.toDomain(): FoodInfoWithId = FoodInfoWithId(
    type = type,
    foodId = foodId,
    foodName = foodName,
    nutrientInfo = nutrientInfo,
    unit = unit,
)

fun FoodWithServingSizeResponse.toDomain(): FoodInfoWithId = FoodInfoWithId(
    type = type,
    foodId = foodId,
    foodName = foodName,
    nutrientInfo = nutrientInfo,
    unit = unit,
    servingSize = servingSize,
)