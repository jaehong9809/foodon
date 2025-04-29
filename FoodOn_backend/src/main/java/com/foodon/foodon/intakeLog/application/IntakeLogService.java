package com.foodon.foodon.intakeLog.application;

import com.foodon.foodon.intakeLog.domain.IntakeLog;
import com.foodon.foodon.intakeLog.dto.IntakeInfoResponse;
import com.foodon.foodon.intakeLog.exception.IntakeLogErrorCode;
import com.foodon.foodon.intakeLog.exception.IntakeLogException;
import com.foodon.foodon.intakeLog.exception.IntakeLogException.IntakeLogBadRequestException;
import com.foodon.foodon.intakeLog.repository.IntakeLogRepository;
import com.foodon.foodon.meal.domain.Meal;
import com.foodon.foodon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static com.foodon.foodon.intakeLog.exception.IntakeLogErrorCode.ILLEGAL_DATE_FORMAT;

@Service
@RequiredArgsConstructor
public class IntakeLogService {

    private final IntakeLogRepository intakeLogRepository;

    public List<IntakeInfoResponse> getIntakeLogsByMonth(
            String date,
            Member member
    ) {
        List<IntakeLog> intakeLogs = findIntakeLogsByMonth(member, date);

        return intakeLogs.stream().map(IntakeInfoResponse::of).toList();
    }

    private List<IntakeLog> findIntakeLogsByMonth(Member member, String date) {
        validateYearMonthFormat(date); // 날짜 요청 형식 검증
        YearMonth yearMonth = YearMonth.parse(date);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        return intakeLogRepository.findByMemberAndDateBetween(member, startDate, endDate);
    }

    private void validateYearMonthFormat(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            YearMonth.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new IntakeLogBadRequestException(ILLEGAL_DATE_FORMAT);
        }
    }

}
