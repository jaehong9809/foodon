package com.foodon.foodon.meal.dto;

import java.util.List;

public record MealInfoResponse(
        String imageFileName,
        int totalKcal,
        int totalCarbs,
        int totalProtein,
        int totalFat,
        List<MealItemInfo> mealItems
) {
    public static MealInfoResponse from(
            String imageFileName,
            int totalKcal,
            int totalCarbs,
            int totalProtein,
            int totalFat,
            List<MealItemInfo> mealItems
    ) {

        return new MealInfoResponse(
                imageFileName,
                totalKcal,
                totalCarbs,
                totalProtein,
                totalFat,
                mealItems
        );
    }
}
