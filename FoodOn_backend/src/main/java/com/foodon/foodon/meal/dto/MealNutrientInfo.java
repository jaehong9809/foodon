package com.foodon.foodon.meal.dto;

import com.foodon.foodon.food.domain.NutrientType;
import com.foodon.foodon.food.domain.NutrientUnit;
import com.foodon.foodon.food.dto.NutrientInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.foodon.foodon.common.util.BigDecimalUtil.divide;
import static com.foodon.foodon.common.util.BigDecimalUtil.multiply;

public record MealNutrientInfo(
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
    public static MealNutrientInfo from(List<NutrientInfo> nutrients) {
        Map<NutrientType, BigDecimal> nutrientMap = toNutrientMapByType(nutrients);

        return new MealNutrientInfo(
                nutrientMap.getOrDefault(NutrientType.KCAL, BigDecimal.ZERO),
                nutrientMap.getOrDefault(NutrientType.CARBS, BigDecimal.ZERO),
                nutrientMap.getOrDefault(NutrientType.SUGAR, BigDecimal.ZERO),
                nutrientMap.getOrDefault(NutrientType.FIBER, BigDecimal.ZERO),
                nutrientMap.getOrDefault(NutrientType.PROTEIN, BigDecimal.ZERO),
                nutrientMap.getOrDefault(NutrientType.FAT, BigDecimal.ZERO),
                nutrientMap.getOrDefault(NutrientType.SATURATED_FAT, BigDecimal.ZERO),
                nutrientMap.getOrDefault(NutrientType.TRANS_FAT, BigDecimal.ZERO),
                nutrientMap.getOrDefault(NutrientType.FATTY_ACID, BigDecimal.ZERO),
                nutrientMap.getOrDefault(NutrientType.UNSATURATED_FAT, BigDecimal.ZERO),
                nutrientMap.getOrDefault(NutrientType.CHOLESTEROL, BigDecimal.ZERO),
                nutrientMap.getOrDefault(NutrientType.SODIUM, BigDecimal.ZERO),
                nutrientMap.getOrDefault(NutrientType.POTASSIUM, BigDecimal.ZERO),
                nutrientMap.getOrDefault(NutrientType.ALCOHOL, BigDecimal.ZERO)
        );
    }

    private static Map<NutrientType, BigDecimal> toNutrientMapByType(List<NutrientInfo> nutrients) {
        return nutrients.stream()
                .collect(Collectors.toMap(
                        info -> NutrientType.from(info.nutrientType()),
                        info -> convertValueByUnit(info.value(), info.nutrientUnit())
                ));
    }

    private static BigDecimal convertValueByUnit(BigDecimal value, NutrientUnit unit) {
        if (value == null) return BigDecimal.ZERO;

        return unit.equals(NutrientUnit.GRAM)
                ? multiply(value, BigDecimal.valueOf(1000))
                : value;
    }

}
