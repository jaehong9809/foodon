package com.foodon.foodon.intakelog.application;

import com.foodon.foodon.common.util.NutrientGoal2;
import com.foodon.foodon.intakelog.domain.IntakeLog;
import com.foodon.foodon.intakelog.dto.IntakeDetailResponse;
import com.foodon.foodon.intakelog.dto.IntakeSummaryResponse;
import com.foodon.foodon.intakelog.exception.IntakeLogException.IntakeLogBadRequestException;
import com.foodon.foodon.intakelog.repository.IntakeLogRepository;
import com.foodon.foodon.meal.domain.Meal;
import com.foodon.foodon.nutrientplan.domain.NutrientPlan;
import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.nutrientplan.repository.NutrientPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.foodon.foodon.common.util.NutrientCalculator.calculateNutrientTarget;
import static com.foodon.foodon.intakelog.exception.IntakeLogErrorCode.*;

@Service
@RequiredArgsConstructor
public class IntakeLogService {

    private final IntakeLogRepository intakeLogRepository;
    private final NutrientPlanRepository nutrientPlanRepository;


    @Transactional
    public void saveIntakeLog(Member member, Meal meal) {
        LocalDate date = meal.getMealTime().toLocalDate();
        IntakeLog intakeLog = findIntakeLogByDate(member, date)
                .orElseGet(() -> {
                    BigDecimal goalKcal = BigDecimal.valueOf(100); // 목표 섭취량 계산 로직
                    return IntakeLog.createIntakeLogOfMember(member, date, goalKcal);
                });

        intakeLog.updateIntakeFromMeal(meal);
        intakeLogRepository.save(intakeLog);
    }

    public List<IntakeSummaryResponse> getIntakeLogsByMonth(
            String date,
            Member member
    ) {
        List<IntakeLog> intakeLogs = findIntakeLogsByMonth(member, date);

        return intakeLogs.stream().map(IntakeSummaryResponse::of).toList();
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

    public IntakeDetailResponse getIntakeDailyDetail(
            LocalDate date,
            Member member
    ) {
        IntakeLog intakeLog = findIntakeLogByDate(member, date);
        NutrientPlan nutrientPlan = findNutrientPlanById(member.getNutrientPlanId());
        NutrientGoal2 nutrientTarget = calculateNutrientTarget(intakeLog.getIntakeKcal(), nutrientPlan);

        return IntakeDetailResponse.from(nutrientTarget, intakeLog);
    }

    private NutrientPlan findNutrientPlanById(Long nutrientPlanId) {
        return nutrientPlanRepository.findById(nutrientPlanId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 관리 유형이 존재하지 않습니다. id = " + nutrientPlanId));
    }

    private Optional<IntakeLog> findIntakeLogByDate(Member member, LocalDate date) {
        return intakeLogRepository.findByMemberAndDate(member, date);
    }

    public IntakeSummaryResponse getIntakeLogByTargetDate(LocalDate date, Member member) {
        return IntakeSummaryResponse.of(findIntakeLogByDate(member, date));
    }

}
