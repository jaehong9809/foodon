package com.foodon.foodon.intakelog.application;

import com.foodon.foodon.activitylevel.domain.ActivityLevel;
import com.foodon.foodon.activitylevel.repository.ActivityLevelRepository;
import com.foodon.foodon.common.util.NutrientGoal;
import com.foodon.foodon.intakelog.domain.IntakeLog;
import com.foodon.foodon.intakelog.dto.IntakeDetailResponse;
import com.foodon.foodon.intakelog.dto.IntakeSummaryResponse;
import com.foodon.foodon.intakelog.exception.IntakeLogException.IntakeLogBadRequestException;
import com.foodon.foodon.intakelog.repository.IntakeLogRepository;
import com.foodon.foodon.meal.domain.Meal;
import com.foodon.foodon.member.domain.MemberStatus;
import com.foodon.foodon.member.repository.MemberStatusRepository;
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

import static com.foodon.foodon.intakelog.exception.IntakeLogErrorCode.*;

@Service
@RequiredArgsConstructor
public class IntakeLogService {

    private final IntakeLogRepository intakeLogRepository;
    private final NutrientPlanRepository nutrientPlanRepository;
    private final ActivityLevelRepository activityLevelRepository;
    private final MemberStatusRepository memberStatusRepository;


    @Transactional
    public void saveIntakeLog(Member member, Meal meal) {
        LocalDate date = meal.getMealTime().toLocalDate();
        IntakeLog intakeLog = findOrCreateIntakeLog(member, date);
        intakeLog.updateIntakeFromMeal(meal);
        intakeLogRepository.save(intakeLog);
    }

    private IntakeLog findOrCreateIntakeLog(Member member, LocalDate date) {
        return findIntakeLogByDate(member, date)
                .orElseGet(() -> createIntakeLog(member, date));
    }

    private IntakeLog createIntakeLog(Member member, LocalDate date) {
        MemberStatus memberStatus = findMemberStatusByMemberId(member.getId());
        ActivityLevel activityLevel = findActivityLevelById(memberStatus.getActivityLevelId());
        BigDecimal goalKcal = NutrientGoal.calculateGoalKcal(member, memberStatus, activityLevel);
        return IntakeLog.createIntakeLogOfMember(member, date, goalKcal);
    }

    public List<IntakeSummaryResponse> getIntakeLogsByMonth(
            String date,
            Member member
    ) {
        List<IntakeLog> intakeLogs = findIntakeLogsByMonth(member, date);

        return intakeLogs.stream().map(IntakeSummaryResponse::withIntakeLog).toList();
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
        Optional<IntakeLog> intakeLogOpt = findIntakeLogByDate(member, date);

        if(intakeLogOpt.isPresent()) {
            MemberStatus memberStatus = findMemberStatusByMemberId(member.getId());
            NutrientPlan nutrientPlan = findNutrientPlanById(memberStatus.getNutrientPlanId());
            NutrientGoal nutrientGoal = NutrientGoal.from(intakeLogOpt.get().getGoalKcal(), nutrientPlan);
            return IntakeDetailResponse.withIntakeLog(nutrientGoal, intakeLogOpt.get(), date);
        } else {
            NutrientGoal nutrientGoal = getNutrientGoalFromMemberStatus(member);
            return IntakeDetailResponse.withOutIntakeLog(nutrientGoal, date);
        }
    }

    public IntakeSummaryResponse getIntakeLogByTargetDate(LocalDate date, Member member) {
        Optional<IntakeLog> intakeLogOpt = findIntakeLogByDate(member, date);

        if(intakeLogOpt.isPresent()) {
            return IntakeSummaryResponse.withIntakeLog(intakeLogOpt.get());
        } else {
          BigDecimal goalKcal = getGoalKcalFromMemberStatus(member);
            return IntakeSummaryResponse.withoutIntakeLog(goalKcal, date);
        }
    }

    private NutrientGoal getNutrientGoalFromMemberStatus(Member member) {
        MemberStatus memberStatus = findMemberStatusByMemberId(member.getId());
        ActivityLevel activityLevel = findActivityLevelById(memberStatus.getActivityLevelId());
        NutrientPlan nutrientPlan = findNutrientPlanById(memberStatus.getNutrientPlanId());
        return NutrientGoal.from(member, memberStatus, activityLevel, nutrientPlan);
    }

    private BigDecimal getGoalKcalFromMemberStatus(Member member) {
        MemberStatus memberStatus = findMemberStatusByMemberId(member.getId());
        ActivityLevel activityLevel = findActivityLevelById(memberStatus.getActivityLevelId());
        return NutrientGoal.calculateGoalKcal(member, memberStatus, activityLevel);
    }

    private NutrientPlan findNutrientPlanById(Long nutrientPlanId) {
        return nutrientPlanRepository.findById(nutrientPlanId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 관리 유형이 존재하지 않습니다. id = " + nutrientPlanId));
    }

    private Optional<IntakeLog> findIntakeLogByDate(Member member, LocalDate date) {
        return intakeLogRepository.findByMemberAndDate(member, date);
    }

    private MemberStatus findMemberStatusByMemberId(Long memberId) {
        return memberStatusRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 유저의 상태관리가 존재하지 않습니다. memberId = " + memberId));
    }

    private ActivityLevel findActivityLevelById(Long activityLevelId) {
        return activityLevelRepository.findById(activityLevelId)
                .orElseThrow(() -> new NoSuchElementException(("해당 ID의 활동량 유형이 존재하지 않습니다. activityLevelId = " + activityLevelId)));
    }

}
