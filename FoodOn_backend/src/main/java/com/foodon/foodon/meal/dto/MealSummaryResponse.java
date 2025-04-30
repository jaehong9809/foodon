package com.foodon.foodon.meal.dto;

import com.foodon.foodon.meal.domain.Meal;
import com.foodon.foodon.meal.domain.MealItem;
import com.foodon.foodon.meal.domain.MealTimeType;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.foodon.foodon.common.util.BigDecimalUtil.toRoundedInt;

public record MealSummaryResponse(
        String imageUrl,
        MealTimeType mealTimeType,
        String mealTime,
        int totalKcal,
        int totalCarbs,
        int totalProtein,
        int totalFat,
        List<String> mealItems
) {
    public static MealSummaryResponse of(Meal meal) {
        return new MealSummaryResponse(
                meal.getMealImage(),
                meal.getMealTimeType(),
                meal.getMealTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                toRoundedInt(meal.getTotalKcal()),
                toRoundedInt(meal.getTotalCarbs()),
                toRoundedInt(meal.getTotalProtein()),
                toRoundedInt(meal.getTotalFat()),
                meal.getMealItems().stream().map(MealItem::getFoodName).toList()
        );
    }
}
