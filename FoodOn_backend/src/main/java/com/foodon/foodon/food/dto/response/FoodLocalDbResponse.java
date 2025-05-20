package com.foodon.foodon.food.dto.response;


public record FoodLocalDbResponse(
        Long foodId,
        String foodName,
        String servingUnit,
        int kcal,
        boolean isCustom
) {
}
