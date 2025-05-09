package com.foodon.foodon.recommend.domain.nutrientclaims;

import com.foodon.foodon.food.domain.NutrientCode;

import java.math.BigDecimal;
import java.util.Map;

public class LowSugarRule implements NutrientClaimRule {

    @Override
    public NutrientCode getNutrientCode() {
        return NutrientCode.SUGAR;
    }

    @Override
    public Level getLevel() {
        return Level.LOW;
    }

    @Override
    public boolean matches(Map<NutrientCode, NutrientServingInfo> nutrientMap) {
        NutrientServingInfo sugarInfo = nutrientMap.get(NutrientCode.SUGAR);
        return sugarInfo.per100g().compareTo(BigDecimal.valueOf(5)) < 0;
    }
}

