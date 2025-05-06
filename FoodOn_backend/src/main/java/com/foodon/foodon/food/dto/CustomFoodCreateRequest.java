package com.foodon.foodon.food.dto;

import com.foodon.foodon.food.domain.Unit;
import com.foodon.foodon.meal.dto.NutrientProfile;

import java.math.BigDecimal;

public record CustomFoodCreateRequest(
        String foodName,
        Unit unit,
        BigDecimal servingSize, // 중량
        NutrientProfile nutrients
) {
}
