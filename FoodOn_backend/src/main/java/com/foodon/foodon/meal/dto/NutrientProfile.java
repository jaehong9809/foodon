package com.foodon.foodon.meal.dto;

import com.foodon.foodon.food.domain.NutrientCode;

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
    public static NutrientProfile from(Map<NutrientCode, BigDecimal> nutrientMap) {

        return new NutrientProfile(
                getOrZero(nutrientMap, NutrientCode.KCAL),
                getOrZero(nutrientMap, NutrientCode.CARBS),
                getOrZero(nutrientMap, NutrientCode.SUGAR),
                getOrZero(nutrientMap, NutrientCode.FIBER),
                getOrZero(nutrientMap, NutrientCode.PROTEIN),
                getOrZero(nutrientMap, NutrientCode.FAT),
                getOrZero(nutrientMap, NutrientCode.SATURATED_FAT),
                getOrZero(nutrientMap, NutrientCode.TRANS_FAT),
                getOrZero(nutrientMap, NutrientCode.FATTY_ACID),
                getOrZero(nutrientMap, NutrientCode.UNSATURATED_FAT),
                getOrZero(nutrientMap, NutrientCode.CHOLESTEROL),
                getOrZero(nutrientMap, NutrientCode.SODIUM),
                getOrZero(nutrientMap, NutrientCode.POTASSIUM),
                getOrZero(nutrientMap, NutrientCode.ALCOHOL)
        );
    }

    public Map<NutrientCode, BigDecimal> toMap() {
        return Map.ofEntries(
                Map.entry(NutrientCode.KCAL, this.kcal),
                Map.entry(NutrientCode.CARBS, this.carbs),
                Map.entry(NutrientCode.SUGAR, this.sugar),
                Map.entry(NutrientCode.FIBER, this.fiber),
                Map.entry(NutrientCode.PROTEIN, this.protein),
                Map.entry(NutrientCode.FAT, this.fat),
                Map.entry(NutrientCode.SATURATED_FAT, this.saturatedFat),
                Map.entry(NutrientCode.TRANS_FAT, this.transFat),
                Map.entry(NutrientCode.FATTY_ACID, this.fattyAcid),
                Map.entry(NutrientCode.UNSATURATED_FAT, this.unsaturatedFat),
                Map.entry(NutrientCode.CHOLESTEROL, this.cholesterol),
                Map.entry(NutrientCode.SODIUM, this.sodium),
                Map.entry(NutrientCode.POTASSIUM, this.potassium),
                Map.entry(NutrientCode.ALCOHOL, this.alcohol)
        );
    }

    private static BigDecimal getOrZero(Map<NutrientCode, BigDecimal> map, NutrientCode code) {
        return map.getOrDefault(code, BigDecimal.ZERO);
    }

}
