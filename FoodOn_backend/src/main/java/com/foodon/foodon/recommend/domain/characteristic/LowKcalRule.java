package com.foodon.foodon.recommend.domain.characteristic;

import com.foodon.foodon.food.domain.NutrientCode;

import java.math.BigDecimal;
import java.util.List;

public class LowKcalRule implements FoodCharacteristicRule{
    @Override
    public NutrientCode getNutrientCode() {
        return NutrientCode.KCAL;
    }

    @Override
    public Level getLevel() {
        return Level.LOW;
    }

    @Override
    public boolean matches(List<NutrientServingInfo> nutrientServingInfos) {
        return getNutrientValue(nutrientServingInfos)
                .map(kcalInfo -> kcalInfo.per100g().compareTo(BigDecimal.valueOf(40)) < 0)
                .orElse(false);
    }
}

