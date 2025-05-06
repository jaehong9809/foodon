package com.foodon.foodon.meal.dto;

import com.foodon.foodon.food.domain.NutrientType;
import com.foodon.foodon.food.domain.Unit;
import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.food.dto.FoodWithNutrientInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record MealItemInfo(
        FoodType type,
        Long foodId,
        String foodName,
        Unit unit,
        BigDecimal quantity,
        NutrientProfile nutrientInfo,
        List<PositionInfo> positions
) {

    public static MealItemInfo from(
            FoodWithNutrientInfo food,
            BigDecimal quantity,
            List<PositionInfo> positions,
            Map<NutrientType, BigDecimal> nutrientMap
    ) {

        return new MealItemInfo(
                food.type(),
                food.foodId(),
                food.foodName(),
                food.unit(),
                quantity,
                NutrientProfile.from(nutrientMap),
                positions
        );
    }
}
