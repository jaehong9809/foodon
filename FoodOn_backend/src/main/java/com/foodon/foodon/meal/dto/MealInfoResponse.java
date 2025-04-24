package com.foodon.foodon.meal.dto;

import java.util.List;

public record MealInfoResponse(
        String imageUrl,
        int totalKcal,
        int totalCarbs,
        int totalProtein,
        int totalFat,
        List<MealItemInfoResponse> mealItems
) {

    public static MealInfoResponse from(
            String imageUrl,
            int totalKcal,
            int totalCarbs,
            int totalProtein,
            int totalFat,
            List<MealItemInfoResponse> mealItems
    ) {

        return new MealInfoResponse(
                imageUrl,
                totalKcal,
                totalCarbs,
                totalProtein,
                totalFat,
                mealItems
        );
    }
}
