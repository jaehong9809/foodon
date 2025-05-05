package com.foodon.foodon.food.dto;

import com.foodon.foodon.food.domain.Unit;

import java.math.BigDecimal;
import java.util.List;

public record CustomFoodCreateRequest(
        String foodName,
        Unit unit,
        BigDecimal servingSize, // 중량
        List<NutrientInfo> nutrients
) {
    public record NutrientInfo(
            Long id,
            BigDecimal value
    ) {}
}
