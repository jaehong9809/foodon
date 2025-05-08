package com.foodon.foodon.intakelog.dto;

import com.foodon.foodon.common.util.BigDecimalUtil;
import com.foodon.foodon.common.util.NutrientGoal;
import com.foodon.foodon.intakelog.domain.IntakeLog;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static com.foodon.foodon.common.util.BigDecimalUtil.toRoundedInt;

public record IntakeDetailResponse(
        LocalDate date,
        BigDecimal goalKcal,
        BigDecimal intakeKcal,
        BigDecimal intakeCarbs,
        BigDecimal goalCarbs,
        BigDecimal intakeProtein,
        BigDecimal goalProtein,
        BigDecimal intakeFat,
        BigDecimal goalFat
) {
    public static IntakeDetailResponse withIntakeLog(
            NutrientGoal nutrientGoal,
            IntakeLog intakeLog,
            LocalDate date
    ) {
        return new IntakeDetailResponse(
                date,
                nutrientGoal.getGoalKcal(),
                intakeLog.getIntakeKcal(),
                nutrientGoal.getGoalCarbs(),
                intakeLog.getIntakeCarbs(),
                nutrientGoal.getGoalProtein(),
                intakeLog.getIntakeProtein(),
                nutrientGoal.getGoalFat(),
                intakeLog.getIntakeFat()
        );
    }

    public static IntakeDetailResponse withOutIntakeLog(
            NutrientGoal nutrientGoal,
            LocalDate date
    ) {
        return new IntakeDetailResponse(
                date,
                nutrientGoal.getGoalKcal(),
                BigDecimal.ZERO,
                nutrientGoal.getGoalCarbs(),
                BigDecimal.ZERO,
                nutrientGoal.getGoalProtein(),
                BigDecimal.ZERO,
                nutrientGoal.getGoalFat(),
                BigDecimal.ZERO
        );
    }
}
