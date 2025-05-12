package com.foodon.foodon.food.dto;

import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.food.domain.Unit;

import java.math.BigDecimal;

public record FoodInfo(
        Long id,
        Long memberId,
        String name,
        Unit unit,
        BigDecimal servingSize
) {
}
