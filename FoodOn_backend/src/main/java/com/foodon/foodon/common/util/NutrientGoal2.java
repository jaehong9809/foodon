package com.foodon.foodon.common.util;

import java.math.BigDecimal;

public record NutrientGoal2(
        BigDecimal goalCarbs,
        BigDecimal goalProtein,
        BigDecimal goalFat
) {
}
