package com.foodon.foodon.intakeLog.dto;

import com.foodon.foodon.intakeLog.domain.IntakeLog;

import java.time.LocalDate;

import static com.foodon.foodon.common.util.NutrientCalculator.toRoundedInt;

public record IntakeInfoResponse(
        Long intakeLogId,
        LocalDate date,
        int intakeKcal,
        int goalKcal
) {
    public static IntakeInfoResponse of(IntakeLog intakeLog) {
        return new IntakeInfoResponse(
                intakeLog.getId(),
                intakeLog.getDate(),
                toRoundedInt(intakeLog.getIntakeKcal()),
                toRoundedInt(intakeLog.getGoalKcal())
        );
    }
}
