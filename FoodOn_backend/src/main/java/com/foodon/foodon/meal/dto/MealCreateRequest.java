package com.foodon.foodon.meal.dto;

import com.foodon.foodon.meal.domain.MealTimeType;

import java.util.List;

public record MealCreateRequest(
        String imageUrl,
        int totalKcal,
        int totalCarbs,
        int totalProtein,
        int totalFat,
        MealTimeType mealTimeType,
        String mealTime,
        List<MealItemInfo> mealItems
) {
}
