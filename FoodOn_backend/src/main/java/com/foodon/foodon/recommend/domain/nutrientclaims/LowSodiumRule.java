package com.foodon.foodon.recommend.domain.nutrientclaims;

import com.foodon.foodon.food.domain.NutrientCode;

import java.math.BigDecimal;
import java.util.Map;

public class LowSodiumRule implements NutrientClaimRule {
    @Override
    public NutrientCode getNutrientCode() {
        return NutrientCode.SODIUM;
    }

    @Override
    public Level getLevel() {
        return Level.LOW;
    }

    @Override
    public boolean matches(Map<NutrientCode, NutrientServingInfo> nutrientMap) {
        NutrientServingInfo sodiumInfo = nutrientMap.get(NutrientCode.SODIUM);
        return sodiumInfo.per100g().compareTo(BigDecimal.valueOf(120)) < 0;
    }
}

