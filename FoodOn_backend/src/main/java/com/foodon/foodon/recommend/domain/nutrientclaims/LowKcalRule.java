package com.foodon.foodon.recommend.domain.nutrientclaims;

import com.foodon.foodon.food.domain.NutrientCode;

import java.math.BigDecimal;
import java.util.Map;

public class LowKcalRule implements NutrientClaimRule {
    @Override
    public NutrientCode getNutrientCode() {
        return NutrientCode.KCAL;
    }

    @Override
    public Level getLevel() {
        return Level.LOW;
    }

    @Override
    public boolean matches(Map<NutrientCode, NutrientServingInfo> nutrientMap) {
        NutrientServingInfo kcalInfo = nutrientMap.get(NutrientCode.KCAL);
        return kcalInfo.per100g().compareTo(BigDecimal.valueOf(40)) < 0;
    }
}

