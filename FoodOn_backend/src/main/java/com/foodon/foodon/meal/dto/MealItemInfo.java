package com.foodon.foodon.meal.dto;

import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.food.domain.Food;
import com.foodon.foodon.food.domain.Unit;

import java.math.BigDecimal;
import java.util.List;

public record MealItemInfo(
        FoodType type,
        Long foodId,
        String foodName,
        Unit unit,
        BigDecimal quantity,
        NutrientInfo nutrientInfo,
        List<PositionInfo> positions
) {

    public static MealItemInfo from(
            Food food,
            BigDecimal quantity,
            List<PositionInfo> positions
    ) {

        return new MealItemInfo(
                food.getFoodType(),
                food.getId(),
                food.getName(),
                food.getUnit(),
                quantity,
                NutrientInfo.of(food.getNutrient()),
                positions
        );
    }
}
