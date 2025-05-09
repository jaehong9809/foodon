package com.foodon.foodon.recommend.dto;

import com.foodon.foodon.recommend.domain.RecommendFood;
import com.foodon.foodon.recommend.domain.nutrientclaims.NutrientClaim;

import java.math.BigDecimal;
import java.util.List;

public record RecommendFoodResponse(
        Long foodId,
        String foodName,
        BigDecimal kcal,
        List<NutrientClaim> nutrientClaims
) {
    public static RecommendFoodResponse from(
            RecommendFood recommendFood,
            List<NutrientClaim> nutrientClaims
    ) {
        return new RecommendFoodResponse(
                recommendFood.getFoodId(),
                recommendFood.getFoodName(),
                recommendFood.getKcalPerServing(),
                nutrientClaims
        );
    }
}

