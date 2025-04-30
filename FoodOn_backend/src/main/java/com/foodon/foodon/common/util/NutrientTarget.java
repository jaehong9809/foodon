package com.foodon.foodon.common.util;

import java.math.BigDecimal;

public record NutrientTarget(
        BigDecimal goalCarbs,
        BigDecimal goalProtein,
        BigDecimal goalFat
) {
}
