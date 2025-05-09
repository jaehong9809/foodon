package com.foodon.foodon.recommend.domain.nutrientclaims;

import com.foodon.foodon.food.domain.NutrientCode;

import java.math.BigDecimal;
import java.util.Map;

public class HighKcalRule implements NutrientClaimRule {
    @Override
    public NutrientCode getNutrientCode() {
        return NutrientCode.KCAL;
    }

    @Override
    public Level getLevel() {
        return Level.HIGH;
    }

    @Override
    public boolean matches(Map<NutrientCode, NutrientServingInfo> nutrientMap) {
        NutrientServingInfo kcalInfo = nutrientMap.get(NutrientCode.KCAL);
        return kcalInfo.perServing().compareTo(BigDecimal.valueOf(600)) >= 0;
    }
}

