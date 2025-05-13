package com.foodon.foodon.meal.dto;

public record MealThumbnailInfoResponse(
        Long mealId,
        Long mealItemId,
        String foodName,
        PositionInfo positionInfo
) {
    public static MealThumbnailInfoResponse of(
            MealThumbnailInfo mealThumbnailInfo
    ){
        return new MealThumbnailInfoResponse(
                mealThumbnailInfo.mealId(),
                mealThumbnailInfo.mealItemId(),
                mealThumbnailInfo.foodName(),
                mealThumbnailInfo.positionInfo()
        );
    }
}
