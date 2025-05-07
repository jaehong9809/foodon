package com.foodon.foodon.common.util;

import com.foodon.foodon.activitylevel.domain.ActivityLevel;
import com.foodon.foodon.intakelog.domain.IntakeLog;
import com.foodon.foodon.intakelog.domain.QIntakeLog;
import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.member.domain.MemberStatus;
import com.foodon.foodon.nutrientplan.domain.NutrientPlan;
import lombok.Getter;

import java.math.BigDecimal;

import static com.foodon.foodon.common.util.BigDecimalUtil.divide;
import static com.foodon.foodon.common.util.BigDecimalUtil.multiply;

@Getter
public class NutrientGoal {

    private static final BigDecimal KCAL_PER_GRAM_CARBS = BigDecimal.valueOf(4);
    private static final BigDecimal KCAL_PER_GRAM_PROTEIN = BigDecimal.valueOf(4);
    private static final BigDecimal KCAL_PER_GRAM_FAT = BigDecimal.valueOf(9);

    private final BigDecimal goalKcal;
    private final BigDecimal goalCarbs;
    private final BigDecimal goalProtein;
    private final BigDecimal goalFat;

    private NutrientGoal(
            BigDecimal goalKcal,
            BigDecimal goalCarbs,
            BigDecimal goalProtein,
            BigDecimal goalFat
    ){
        this.goalKcal = goalKcal;
        this.goalCarbs = goalCarbs;
        this.goalProtein = goalProtein;
        this.goalFat = goalFat;
    }

    public static NutrientGoal from(
            IntakeLog intakeLog,
            NutrientPlan nutrientPlan
    ) {
        return new NutrientGoal(
                intakeLog.getGoalKcal(),
                calculateGoalCarbs(intakeLog.getGoalKcal(), nutrientPlan),
                calculateGoalProtein(intakeLog.getGoalKcal(),nutrientPlan),
                calculateGoalFat(intakeLog.getGoalKcal(),nutrientPlan)
        );
    }

    /**
     * 일일 목표 섭취 칼로리 계산
     */
    public static BigDecimal calculateGoalKcal(
            Member member,
            MemberStatus status,
            ActivityLevel activityLevel,
    ){


    }

    /**
     * 총 섭취 칼로리 x 비율 ÷ 열량
     */
    private static BigDecimal calculateGoalByKcal(
            BigDecimal intakeKcal,
            double ratio,
            BigDecimal kcalPerGram
    ) {
        return divide(
                multiply(intakeKcal, BigDecimal.valueOf(ratio)),
                kcalPerGram
        );
    }

    private static BigDecimal calculateGoalCarbs(
            BigDecimal goalKcal,
            NutrientPlan nutrientPlan
    ) {
        return calculateGoalByKcal(goalKcal, nutrientPlan.getCarbsRatio(), KCAL_PER_GRAM_CARBS);
    }

    private static BigDecimal calculateGoalProtein(
            BigDecimal goalKcal,
            NutrientPlan nutrientPlan
    ) {
        return calculateGoalByKcal(goalKcal, nutrientPlan.getProteinRatio(), KCAL_PER_GRAM_PROTEIN);
    }

    private static BigDecimal calculateGoalFat(
            BigDecimal goalKcal,
            NutrientPlan nutrientPlan
    ){
        return calculateGoalByKcal(goalKcal, nutrientPlan.getFatRatio(), KCAL_PER_GRAM_FAT);
    }

}
