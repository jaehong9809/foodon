package com.swallaby.foodon.domain.food.model

import com.swallaby.foodon.data.food.remote.dto.request.CustomFoodRequest

data class FoodInfoWithId(
    val type: FoodType = FoodType.PUBLIC,
    val foodId: Long = 0,
    val foodName: String = "",
    val nutrientInfo: NutrientInfo = NutrientInfo(),
    val servingSize: Int = 0,
    val unit: UnitType = UnitType.GRAM,
)

data class FoodInfo(
    val foodName: String = "",
    val nutrients: NutrientInfo = NutrientInfo(),
    val servingSize: Int = 0,
    val unit: UnitType = UnitType.GRAM,
)


fun FoodInfo.toRequest(): CustomFoodRequest = CustomFoodRequest(
    foodName = foodName,
    nutrients = nutrients,
    servingSize = servingSize,
    unit = unit,
)