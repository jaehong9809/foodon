package com.foodon.foodon.recommend.domain.characteristic;

import com.foodon.foodon.food.domain.NutrientCode;

import java.math.BigDecimal;
import java.util.List;

public class HighKcalRule implements FoodCharacteristicRule{
    @Override
    public NutrientCode getNutrientCode() {
        return NutrientCode.KCAL;
    }

    @Override
    public Level getLevel() {
        return Level.HIGH;
    }

    @Override
    public boolean matches(List<NutrientServingInfo> nutrientServingInfos) {
        return getNutrientValue(nutrientServingInfos)
                .map(kcalInfo -> kcalInfo.perServing().compareTo(BigDecimal.valueOf(600)) >= 0)
                .orElse(false);
    }
}

