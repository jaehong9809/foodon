package com.foodon.foodon.recommend.domain.characteristic;

import com.foodon.foodon.food.domain.NutrientCode;

import java.util.List;
import java.util.Optional;

import static java.util.spi.ToolProvider.findFirst;

public interface FoodCharacteristicRule {
    NutrientCode getNutrientCode();
    Level getLevel();
    boolean matches(List<NutrientServingInfo> nutrientServingInfos);

    default Optional<NutrientServingInfo> getNutrientValue(List<NutrientServingInfo> nutrientServingInfos) {
        return nutrientServingInfos.stream()
                .filter(n -> n.nutrientCode().equals(getNutrientCode()))
                .findFirst();
    }
}

