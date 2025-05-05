package com.foodon.foodon.meal.dto;

import com.foodon.foodon.food.domain.Unit;
import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.food.dto.FoodWithNutrientInfo;

import java.math.BigDecimal;
import java.util.List;

public record MealItemInfo(
        FoodType type,
        Long foodId,
        String foodName,
        Unit unit,
        BigDecimal quantity,
        MealNutrientInfo nutrientInfo,
        List<PositionInfo> positions
) {

    public static MealItemInfo from(
            FoodWithNutrientInfo food,
            BigDecimal quantity,
            List<PositionInfo> positions
    ) {

        return new MealItemInfo(
                food.type(),
                food.foodId(),
                food.foodName(),
                food.unit(),
                quantity,
                MealNutrientInfo.from(food.nutrients()),
                positions
        );
    }
}
