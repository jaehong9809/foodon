package com.foodon.foodon.meal.client;

import java.util.List;

public record MealDetectAiResponse(
        List<FoodInfo> foods
) {

    public record FoodInfo(
            String name,
            int count,
            List<PositionInfo> positions
    ) {
    }

    public record PositionInfo(
            double x,
            double y,
            double width,
            double height
    ) {
    }
}
