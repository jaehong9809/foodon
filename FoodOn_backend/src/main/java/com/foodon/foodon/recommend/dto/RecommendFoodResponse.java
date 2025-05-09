package com.foodon.foodon.recommend.dto;

import com.foodon.foodon.recommend.domain.RecommendFood;
import com.foodon.foodon.recommend.domain.characteristic.FoodCharacteristic;

import java.math.BigDecimal;
import java.util.List;

public record RecommendFoodResponse(
        Long foodId,
        String foodName,
        BigDecimal kcal,
        List<FoodCharacteristic> characteristics
) {
    public static RecommendFoodResponse from(
            RecommendFood recommendFood,
            List<FoodCharacteristic> characteristics
    ) {
        return new RecommendFoodResponse(
                recommendFood.getFoodId(),
                recommendFood.getFoodName(),
                recommendFood.getKcalPerServing(),
                characteristics
        );
    }
}

