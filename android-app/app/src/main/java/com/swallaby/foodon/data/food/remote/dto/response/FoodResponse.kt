package com.swallaby.foodon.data.food.remote.dto.response

import com.swallaby.foodon.domain.food.model.FoodInfoWithId
import com.swallaby.foodon.domain.food.model.FoodType
import com.swallaby.foodon.domain.food.model.NutrientInfo
import com.swallaby.foodon.domain.food.model.UnitType

/*
    "type": "PUBLIC",
    "foodId": 0,
    "foodName": "string",
    "unit": "SLICE",
 */
data class FoodResponse(
    val type: FoodType = FoodType.PUBLIC,
    val foodId: Long,
    val foodName: String = "",
    val nutrientInfo: NutrientInfo = NutrientInfo(),
//    val servingSize: Int = 0,
    val unit: UnitType = UnitType.GRAM,
)

fun FoodResponse.toDomain(): FoodInfoWithId = FoodInfoWithId(
    type = type,
    foodId = foodId,
    foodName = foodName,
    nutrientInfo = nutrientInfo,
//    servingSize = servingSize,
    unit = unit,
)