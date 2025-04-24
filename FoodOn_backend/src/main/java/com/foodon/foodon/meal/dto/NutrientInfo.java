package com.foodon.foodon.meal.dto;

import com.foodon.foodon.food.domain.Nutrient;

import java.math.BigDecimal;

public record NutrientInfo(
        BigDecimal kcal,
        BigDecimal carbs,
        BigDecimal sugar,
        BigDecimal fiber,
        BigDecimal protein,
        BigDecimal fat,
        BigDecimal saturatedFat,
        BigDecimal transFat,
        BigDecimal fattyAcid,
        BigDecimal unsaturatedFat,
        BigDecimal cholesterol,
        BigDecimal sodium,
        BigDecimal potassium,
        BigDecimal alcohol
) {

    public static NutrientInfo of(Nutrient nutrient) {
        return new NutrientInfo(
                nutrient.getKcal(),
                nutrient.getCarbs(),
                nutrient.getSugar(),
                nutrient.getFiber(),
                nutrient.getProtein(),
                nutrient.getFat(),
                nutrient.getSaturatedFat(),
                nutrient.getTransFat(),
                nutrient.getFattyAcid(),
                nutrient.getUnsaturatedFat(),
                nutrient.getCholesterol(),
                nutrient.getSodium(),
                nutrient.getPotassium(),
                nutrient.getAlcohol()
        );
    }
}
