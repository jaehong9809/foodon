package com.foodon.foodon.intakelog.dto;

import com.foodon.foodon.common.util.NutrientTarget;
import com.foodon.foodon.intakelog.domain.IntakeLog;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.foodon.foodon.common.util.BigDecimalUtil.round;
import static com.foodon.foodon.common.util.BigDecimalUtil.toRoundedInt;

public record IntakeDetailResponse(
        Long intakeLogId,
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
    public static IntakeDetailResponse from(
            NutrientTarget nutrientTarget,
            IntakeLog intakeLog
    ) {

        return new IntakeDetailResponse(
                intakeLog.getId(),
                intakeLog.getDate(),
                round(intakeLog.getGoalKcal(), 0),
                round(intakeLog.getIntakeKcal(), 0),
                round(nutrientTarget.goalCarbs(), 1),
                round(intakeLog.getIntakeCarbs(), 1),
                round(nutrientTarget.goalProtein(), 1),
                round(intakeLog.getIntakeProtein(), 1),
                round(nutrientTarget.goalFat(), 1),
                round(intakeLog.getIntakeFat(), 1)
        );
    }
}
