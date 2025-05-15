package com.foodon.foodon.meal.dto;

import com.foodon.foodon.meal.domain.Meal;
import com.foodon.foodon.meal.domain.MealTimeType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.foodon.foodon.common.util.BigDecimalUtil.round;

public record MealDetailInfoResponse(
        Long mealId,
        String mealTime,
        MealTimeType mealTimeType,
        String imageFileName,
        BigDecimal totalKcal,
        BigDecimal totalCarbs,
        BigDecimal totalProtein,
        BigDecimal totalFat,
        List<MealItemInfo> mealItems
) {
    public static MealDetailInfoResponse from(
            Meal meal,
            BigDecimal totalKcal,
            BigDecimal totalCarbs,
            BigDecimal totalProtein,
            BigDecimal totalFat,
            List<MealItemInfo> mealItems
    ) {
        return new MealDetailInfoResponse(
                meal.getId(),
                meal.getMealTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                meal.getMealTimeType(),
                meal.getMealImage(),
                round(totalKcal, 0),
                round(totalCarbs, 1),
                round(totalProtein, 1),
                round(totalFat, 1),
                mealItems
        );
    }
}
