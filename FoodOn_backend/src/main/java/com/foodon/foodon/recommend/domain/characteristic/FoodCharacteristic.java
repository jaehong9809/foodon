package com.foodon.foodon.recommend.domain.characteristic;

import com.foodon.foodon.food.domain.NutrientCode;

public record FoodCharacteristic(
        NutrientCode nutrientCode,
        Level level
) {
    public static FoodCharacteristic from(
            FoodCharacteristicRule foodCharacteristicRule
    ) {
        return new FoodCharacteristic(
                foodCharacteristicRule.getNutrientCode(),
                foodCharacteristicRule.getLevel()
        );
    }
}

