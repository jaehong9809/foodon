package com.foodon.foodon.meal.dto;

import com.foodon.foodon.food.domain.NutrientType;

import java.math.BigDecimal;
import java.util.Map;

public record NutrientProfile(
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
    public static NutrientProfile from(Map<NutrientType, BigDecimal> nutrientMap) {

        return new NutrientProfile(
                getOrZero(nutrientMap, NutrientType.KCAL),
                getOrZero(nutrientMap, NutrientType.CARBS),
                getOrZero(nutrientMap, NutrientType.SUGAR),
                getOrZero(nutrientMap, NutrientType.FIBER),
                getOrZero(nutrientMap, NutrientType.PROTEIN),
                getOrZero(nutrientMap, NutrientType.FAT),
                getOrZero(nutrientMap, NutrientType.SATURATED_FAT),
                getOrZero(nutrientMap, NutrientType.TRANS_FAT),
                getOrZero(nutrientMap, NutrientType.FATTY_ACID),
                getOrZero(nutrientMap, NutrientType.UNSATURATED_FAT),
                getOrZero(nutrientMap, NutrientType.CHOLESTEROL),
                getOrZero(nutrientMap, NutrientType.SODIUM),
                getOrZero(nutrientMap, NutrientType.POTASSIUM),
                getOrZero(nutrientMap, NutrientType.ALCOHOL)
        );
    }

    public Map<NutrientType, BigDecimal> toMap() {
        return Map.ofEntries(
                Map.entry(NutrientType.KCAL, this.kcal),
                Map.entry(NutrientType.CARBS, this.carbs),
                Map.entry(NutrientType.SUGAR, this.sugar),
                Map.entry(NutrientType.FIBER, this.fiber),
                Map.entry(NutrientType.PROTEIN, this.protein),
                Map.entry(NutrientType.FAT, this.fat),
                Map.entry(NutrientType.SATURATED_FAT, this.saturatedFat),
                Map.entry(NutrientType.TRANS_FAT, this.transFat),
                Map.entry(NutrientType.FATTY_ACID, this.fattyAcid),
                Map.entry(NutrientType.UNSATURATED_FAT, this.unsaturatedFat),
                Map.entry(NutrientType.CHOLESTEROL, this.cholesterol),
                Map.entry(NutrientType.SODIUM, this.sodium),
                Map.entry(NutrientType.POTASSIUM, this.potassium),
                Map.entry(NutrientType.ALCOHOL, this.alcohol)
        );
    }

    private static BigDecimal getOrZero(Map<NutrientType, BigDecimal> map, NutrientType key) {
        return map.getOrDefault(key, BigDecimal.ZERO);
    }

}
