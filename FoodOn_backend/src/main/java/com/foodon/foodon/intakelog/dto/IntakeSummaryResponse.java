package com.foodon.foodon.intakelog.dto;

import com.foodon.foodon.intakelog.domain.IntakeLog;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.foodon.foodon.common.util.BigDecimalUtil.round;
import static com.foodon.foodon.common.util.BigDecimalUtil.toRoundedInt;


public record IntakeSummaryResponse(
        Long intakeLogId,
        LocalDate date,
        BigDecimal goalKcal,
        BigDecimal intakeKcal
) {
    public static IntakeSummaryResponse of(IntakeLog intakeLog) {
        return new IntakeSummaryResponse(
                intakeLog.getId(),
                intakeLog.getDate(),
                round(intakeLog.getGoalKcal(), 0),
                round(intakeLog.getIntakeKcal(), 0)
        );
    }
}
