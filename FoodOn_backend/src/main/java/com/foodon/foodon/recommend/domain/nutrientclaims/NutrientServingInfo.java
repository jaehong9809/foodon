package com.foodon.foodon.recommend.domain.nutrientclaims;

import com.foodon.foodon.food.domain.NutrientCode;

import java.math.BigDecimal;

public record NutrientServingInfo(
        NutrientCode nutrientCode,
        BigDecimal per100g,
        BigDecimal perServing
) {
}

