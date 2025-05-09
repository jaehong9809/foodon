package com.foodon.foodon.recommend.domain.characteristic;

import com.foodon.foodon.food.domain.NutrientCode;

import java.math.BigDecimal;
import java.util.List;

import static com.foodon.foodon.common.util.BigDecimalUtil.multiply;

public class HighProteinRule implements FoodCharacteristicRule{
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
    public boolean matches(List<NutrientServingInfo> nutrientServingInfos) {
        return getNutrientValue(nutrientServingInfos)
                .map(proteinInfo -> proteinInfo.per100g().compareTo(multiply(BigDecimal.valueOf(50), BigDecimal.valueOf(0.2))) >= 0)
                .orElse(false);
    }
}

