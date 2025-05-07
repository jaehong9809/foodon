package com.foodon.foodon.meal.dto;

import com.foodon.foodon.common.util.BigDecimalUtil;
import com.foodon.foodon.food.domain.HealthEffect;
import com.foodon.foodon.food.domain.Nutrient;
import com.foodon.foodon.food.domain.NutrientUnit;
import com.foodon.foodon.food.domain.RestrictionType;
import com.foodon.foodon.meal.domain.ManageNutrient;

import java.math.BigDecimal;

public record ManageNutrientResponse(
        String nutrientType,
        RestrictionType restrictionType,
        NutrientUnit unit,
        BigDecimal intake,
        BigDecimal minRecommended,
        BigDecimal maxRecommended,
        HealthEffect healthEffect,
        ManageStatus status
) {
    public static ManageNutrientResponse from(
            Nutrient nutrient,
            BigDecimal intake,
            BigDecimal minRecommended,
            BigDecimal maxRecommended,
            ManageStatus status
    ){
        return new ManageNutrientResponse(
                nutrient.getType(),
                nutrient.getRestrictionType(),
                nutrient.getNutrientUnit(),
                BigDecimalUtil.round(intake, 2),
                BigDecimalUtil.round(minRecommended, 2),
                BigDecimalUtil.round(maxRecommended, 2),
                nutrient.getHealthEffect(),
                status
        );
    }
}
