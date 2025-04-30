package com.foodon.foodon.intakelog.dto;

import com.foodon.foodon.common.util.NutrientTarget;
import com.foodon.foodon.intakelog.domain.IntakeLog;

import java.time.LocalDate;

import static com.foodon.foodon.common.util.BigDecimalUtil.toRoundedInt;

public record IntakeDetailResponse(
        Long intakeLogId,
        LocalDate date,
        int goalKcal,
        int intakeKcal,
        int intakeCarbs,
        int goalCarbs,
        int intakeProtein,
        int goalProtein,
        int intakeFat,
        int goalFat
) {
    public static IntakeDetailResponse from(
            NutrientTarget nutrientTarget,
            IntakeLog intakeLog
    ) {

        return new IntakeDetailResponse(
                intakeLog.getId(),
                intakeLog.getDate(),
                toRoundedInt(intakeLog.getGoalKcal()),
                toRoundedInt(intakeLog.getIntakeKcal()),
                toRoundedInt(nutrientTarget.goalCarbs()),
                toRoundedInt(intakeLog.getIntakeCarbs()),
                toRoundedInt(nutrientTarget.goalProtein()),
                toRoundedInt(intakeLog.getIntakeProtein()),
                toRoundedInt(nutrientTarget.goalFat()),
                toRoundedInt(intakeLog.getIntakeFat())
        );
    }
}
