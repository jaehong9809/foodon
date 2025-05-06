package com.foodon.foodon.meal.dto;

import java.math.BigDecimal;

public record NutrientIntakeInfo(
        Long nutrientId,
        String type,
        BigDecimal intake
) {
}
