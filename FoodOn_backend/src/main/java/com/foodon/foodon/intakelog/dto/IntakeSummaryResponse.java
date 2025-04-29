package com.foodon.foodon.intakelog.dto;

import com.foodon.foodon.intakelog.domain.IntakeLog;

import java.time.LocalDate;

import static com.foodon.foodon.common.util.BigDecimalUtil.toRoundedInt;


public record IntakeSummaryResponse(
        Long intakeLogId,
        LocalDate date,
        int goalKcal,
        int intakeKcal
) {
    public static IntakeSummaryResponse of(IntakeLog intakeLog) {
        return new IntakeSummaryResponse(
                intakeLog.getId(),
                intakeLog.getDate(),
                toRoundedInt(intakeLog.getGoalKcal()),
                toRoundedInt(intakeLog.getIntakeKcal())
        );
    }
}
