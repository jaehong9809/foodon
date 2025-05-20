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
    quantity = 1,
    nutrientInfo = nutrientInfo,
    // todo servingSize 추가
    servingSize = 1.0,
    positions = listOf(
        Position(
            height = 1.0,
            width = 1.0,
        )
    )
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