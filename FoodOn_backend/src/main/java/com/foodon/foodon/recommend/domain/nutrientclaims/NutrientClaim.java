package com.foodon.foodon.recommend.domain.nutrientclaims;

import com.foodon.foodon.food.domain.NutrientCode;

public record NutrientClaim(
        NutrientCode nutrientCode,
        Level level
) {
    public static NutrientClaim from(
            NutrientClaimRule nutrientClaimRule
    ) {
        return new NutrientClaim(
                nutrientClaimRule.getNutrientCode(),
                nutrientClaimRule.getLevel()
        );
    }
}

