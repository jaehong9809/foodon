package com.foodon.foodon.meal.dto;

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
        Double intake,
        Double minRecommended,
        Double maxRecommended,
        HealthEffect healthEffect,
        ManageStatus status
) {
    public static ManageNutrientResponse from(
            Nutrient nutrient,
            BigDecimal intake,
            Double minRecommended,
            Double maxRecommended,
            ManageStatus status
    ){
        return new ManageNutrientResponse(
                nutrient.getType(),
                nutrient.getRestrictionType(),
                nutrient.getNutrientUnit(),
                intake.doubleValue(),
                minRecommended,
                maxRecommended,
                nutrient.getHealthEffect(),
                status
        );
    }
}
