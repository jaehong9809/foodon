package com.foodon.foodon.food.dto;

import com.foodon.foodon.food.domain.Food;
import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.food.domain.NutrientType;
import com.foodon.foodon.food.domain.Unit;
import com.foodon.foodon.meal.dto.NutrientProfile;

import java.math.BigDecimal;
import java.util.Map;

public record FoodDetailInfoResponse(
        FoodType type,
        Long foodId,
        String foodName,
        Unit unit,
        NutrientProfile nutrientInfo
) {
    public static FoodDetailInfoResponse from(
            FoodWithNutrientInfo foodWithNutrientInfo,
            Map<NutrientType, BigDecimal> nutrientMap
    ) {
        return new FoodDetailInfoResponse(
                foodWithNutrientInfo.type(),
                foodWithNutrientInfo.foodId(),
                foodWithNutrientInfo.foodName(),
                foodWithNutrientInfo.unit(),
                NutrientProfile.from(nutrientMap)
        );
    }
}
