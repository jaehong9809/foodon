package com.foodon.foodon.food.dto;

import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.food.domain.Unit;

import java.util.List;

public record FoodWithNutrientInfo(
        FoodType type,
        Long foodId,
        String foodName,
        Unit unit,
        List<NutrientInfo> nutrients
) {
}
