package com.foodon.foodon.food.dto;

import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.food.domain.Unit;

public record FoodInfo(
        FoodType type,
        Long id,
        String name,
        Unit unit
) {
}
