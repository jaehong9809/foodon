package com.foodon.foodon.common.util;

import com.foodon.foodon.nutrientplan.domain.NutrientPlan;
import com.foodon.foodon.meal.dto.MealItemInfo;
import com.foodon.foodon.meal.dto.NutrientInfo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

import static com.foodon.foodon.common.util.BigDecimalUtil.divide;
import static com.foodon.foodon.common.util.BigDecimalUtil.multiply;

@Component
public class NutrientCalculator {

    private static final BigDecimal KCAL_PER_GRAM_CARBS = BigDecimal.valueOf(4);
    private static final BigDecimal KCAL_PER_GRAM_PROTEIN = BigDecimal.valueOf(4);
    private static final BigDecimal KCAL_PER_GRAM_FAT = BigDecimal.valueOf(9);

    /**
     * 영양 성분 섭취량 총합 계산
     */
    public static BigDecimal sum(
            List<MealItemInfo> items,
            Function<NutrientInfo, BigDecimal> getter
    ) {
        return items.stream()
                .map(item -> multiply(getter.apply(item.nutrientInfo()), item.quantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 영양 성분 일일 목표 섭취량 구하기
     */
    public static NutrientTarget calculateNutrientTarget(
            BigDecimal intakeKcal,
            NutrientPlan managementType
    ) {
        BigDecimal goalCarbs = divide(
                multiply(intakeKcal, BigDecimal.valueOf(managementType.getCarbsRatio())),
                KCAL_PER_GRAM_CARBS
        );

        BigDecimal goalProtein = divide(
                multiply(intakeKcal, BigDecimal.valueOf(managementType.getProteinRatio())),
                KCAL_PER_GRAM_PROTEIN
        );

        BigDecimal goalFat = divide(
                multiply(intakeKcal, BigDecimal.valueOf(managementType.getFatRatio())),
                KCAL_PER_GRAM_FAT
        );

        return new NutrientTarget(goalCarbs, goalProtein, goalFat);
    }

}
