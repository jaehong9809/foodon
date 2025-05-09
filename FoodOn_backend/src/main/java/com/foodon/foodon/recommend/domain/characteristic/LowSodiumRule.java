package com.foodon.foodon.recommend.domain.characteristic;

import com.foodon.foodon.food.domain.NutrientCode;

import java.math.BigDecimal;
import java.util.List;

public class LowSodiumRule implements FoodCharacteristicRule {
    @Override
    public NutrientCode getNutrientCode() {
        return NutrientCode.SODIUM;
    }

    @Override
    public Level getLevel() {
        return Level.LOW;
    }

    @Override
    public boolean matches(List<NutrientServingInfo> nutrientServingInfos) {
        return getNutrientValue(nutrientServingInfos)
                .map(sodiumInfo -> sodiumInfo.per100g().compareTo(BigDecimal.valueOf(120)) < 0)
                .orElse(false);
    }
}

