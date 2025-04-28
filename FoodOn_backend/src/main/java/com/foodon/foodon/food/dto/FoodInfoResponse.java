package com.foodon.foodon.food.dto;

import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.food.domain.Unit;
import com.foodon.foodon.meal.dto.NutrientInfo;

public record FoodInfoResponse(
        FoodType type,
        Long foodId,
        String foodName,
        Unit unit,
        NutrientInfo nutrientInfo
) {
}
