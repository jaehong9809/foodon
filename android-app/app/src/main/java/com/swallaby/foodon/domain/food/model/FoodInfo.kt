package com.swallaby.foodon.domain.food.model

import com.swallaby.foodon.data.food.remote.dto.request.CustomFoodRequest

data class FoodInfoWithId(
    val type: FoodType = FoodType.PUBLIC,
    val foodId: Long = 0,
    val foodName: String = "",
    val unit: UnitType = UnitType.GRAM,
    val nutrientInfo: NutrientInfo = NutrientInfo(),
)

fun FoodInfoWithId.toMealItem() = MealItem(
    type = type,
    foodId = foodId,
    foodName = foodName,
    unit = unit,
    nutrientInfo = nutrientInfo,
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