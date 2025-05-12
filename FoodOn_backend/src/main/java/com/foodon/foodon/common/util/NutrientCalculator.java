package com.foodon.foodon.common.util;

import com.foodon.foodon.food.domain.NutrientUnit;
import com.foodon.foodon.meal.domain.Meal;
import com.foodon.foodon.meal.dto.NutrientProfile;
import com.foodon.foodon.nutrientplan.domain.NutrientPlan;
import com.foodon.foodon.meal.dto.MealItemInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.foodon.foodon.common.util.BigDecimalUtil.divide;
import static com.foodon.foodon.common.util.BigDecimalUtil.multiply;


public class NutrientCalculator {

    private static final BigDecimal KCAL_PER_GRAM_CARBS = BigDecimal.valueOf(4);
    private static final BigDecimal KCAL_PER_GRAM_PROTEIN = BigDecimal.valueOf(4);
    private static final BigDecimal KCAL_PER_GRAM_FAT = BigDecimal.valueOf(9);

    /**
     * 영양 성분 섭취량 총합 계산
     */
    public static BigDecimal sumTotalIntake(
            List<MealItemInfo> items,
            Function<NutrientProfile, BigDecimal> nutrientGetter
    ) {
        return items.stream()
                .map(item -> {
                    BigDecimal valuePerUnit = nutrientGetter.apply(item.nutrientInfo());
                    BigDecimal quantity = item.quantity();
                    return multiply(valuePerUnit, quantity);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 영양 성분 일일 목표 섭취량 구하기
     */
    public static NutrientTarget calculateNutrientTarget(
            BigDecimal intakeKcal,
            NutrientPlan nutrientPlan
    ) {
        return new NutrientTarget(
                calculateTargetGram(intakeKcal, nutrientPlan.getCarbsRatio(), KCAL_PER_GRAM_CARBS),
                calculateTargetGram(intakeKcal, nutrientPlan.getProteinRatio(), KCAL_PER_GRAM_PROTEIN),
                calculateTargetGram(intakeKcal, nutrientPlan.getFatRatio(), KCAL_PER_GRAM_FAT)
        );
    }

    /**
     * 총 섭취 칼로리 x 비율 ÷ 열량
     */
    private static BigDecimal calculateTargetGram(
            BigDecimal intakeKcal,
            double ratio,
            BigDecimal kcalPerGram
    ) {
        return divide(
                multiply(intakeKcal, BigDecimal.valueOf(ratio)),
                kcalPerGram
        );
    }

    public static BigDecimal convertToMilligram(BigDecimal value, NutrientUnit unit) {
        if (value == null) return BigDecimal.ZERO;

        return unit.equals(NutrientUnit.GRAM)
                ? multiply(value, BigDecimal.valueOf(1000))
                : value;
    }

    public static BigDecimal convertToGram(BigDecimal value, NutrientUnit unit) {
        if (value == null) return BigDecimal.ZERO;

        return unit.equals(NutrientUnit.MILLIGRAM)
                ? divide(value, BigDecimal.valueOf(1000))
                : value;
    }

    /**
     * 1회 제공량당 영양성분 함량 계산
     */
    public static BigDecimal calculateNutrientPerServing(
            BigDecimal foodServingSize,
            BigDecimal per100g
    ){
        return divide(per100g, BigDecimal.valueOf(100))
                .multiply(foodServingSize);
    }

}
