package com.foodon.foodon.recommend.domain.nutrientclaims;

import com.foodon.foodon.food.domain.NutrientCode;

import java.util.Map;

public interface NutrientClaimRule {
    NutrientCode getNutrientCode();
    Level getLevel();
    boolean matches(Map<NutrientCode, NutrientServingInfo> nutrientMap);
}

