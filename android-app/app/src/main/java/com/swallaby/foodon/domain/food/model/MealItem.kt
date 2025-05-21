package com.swallaby.foodon.domain.food.model

data class MealItem(
    val type: FoodType = FoodType.PUBLIC,
    val foodId: Long = 0,
    val foodName: String = "",
    val unit: UnitType = UnitType.GRAM,
    val servingSize: Double = 0.0,
    val quantity: Int = 0,
    val nutrientInfo: NutrientInfo = NutrientInfo(),
    val positions: List<Position> = emptyList(),
    // 음식 삭제용
    @Transient val originalFoodId: Long = 0,
)
