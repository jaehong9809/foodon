package com.foodon.foodon.recommend.domain.characteristic;

import com.foodon.foodon.food.domain.NutrientCode;

import java.math.BigDecimal;
import java.util.List;

public class LowSugarRule implements FoodCharacteristicRule {

    @Override
    public NutrientCode getNutrientCode() {
        return NutrientCode.SUGAR;
    }

    @Override
    public Level getLevel() {
        return Level.LOW;
    }

    @Override
    public boolean matches(List<NutrientServingInfo> nutrientServingInfos) {
        return getNutrientValue(nutrientServingInfos)
                .map(sugarInfo -> sugarInfo.per100g().compareTo(BigDecimal.valueOf(5)) < 0)
                .orElse(false);
    }
}

