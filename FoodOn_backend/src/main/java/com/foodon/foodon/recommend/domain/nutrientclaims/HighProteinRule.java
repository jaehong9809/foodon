package com.foodon.foodon.recommend.domain.nutrientclaims;

import com.foodon.foodon.food.domain.NutrientCode;

import java.math.BigDecimal;
import java.util.Map;

public class HighProteinRule implements NutrientClaimRule {
    @Override
    public NutrientCode getNutrientCode() {
        return NutrientCode.PROTEIN;
    }

    @Override
    public Level getLevel() {
        return Level.HIGH;
    }

    /**
     * 식품 100g 당 함량이 1일 권장량의 20% 이상인 경우 고단백
     */
    @Override
    public boolean matches(Map<NutrientCode, NutrientServingInfo> nutrientMap) {
        NutrientServingInfo proteinInfo = nutrientMap.get(NutrientCode.PROTEIN);
        return proteinInfo.per100g().compareTo(BigDecimal.valueOf(10)) >= 0;
    }
}

