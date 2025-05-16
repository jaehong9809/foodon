package com.foodon.foodon.recommend.application;

import com.foodon.foodon.common.util.BigDecimalUtil;
import com.foodon.foodon.common.util.NutrientGoal;
import com.foodon.foodon.food.domain.NutrientCode;
import com.foodon.foodon.food.dto.NutrientClaimInfo;
import com.foodon.foodon.food.dto.NutrientInfo;
import com.foodon.foodon.food.repository.FoodRepository;
import com.foodon.foodon.intakelog.domain.IntakeLog;
import com.foodon.foodon.intakelog.repository.IntakeLogRepository;
import com.foodon.foodon.member.domain.ActivityLevel;
import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.member.domain.MemberStatus;
import com.foodon.foodon.member.domain.NutrientPlan;
import com.foodon.foodon.member.exception.MemberErrorCode;
import com.foodon.foodon.member.exception.MemberException;
import com.foodon.foodon.member.repository.ActivityLevelRepository;
import com.foodon.foodon.member.repository.MemberRepository;
import com.foodon.foodon.member.repository.MemberStatusRepository;
import com.foodon.foodon.member.repository.NutrientPlanRepository;
import com.foodon.foodon.recommend.domain.HistoryIntakeStatus;
import com.foodon.foodon.recommend.domain.RecommendFood;
import com.foodon.foodon.recommend.domain.RecommendReasonFormat;
import com.foodon.foodon.recommend.dto.RecommendFoodResponse;
import com.foodon.foodon.recommend.dto.RecommendedFood;
import com.foodon.foodon.recommend.exception.RecommendFoodException.RecommendFoodBadRequestException;
import com.foodon.foodon.recommend.repository.RecommendFoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import static com.foodon.foodon.recommend.domain.HistoryIntakeStatus.*;
import static com.foodon.foodon.recommend.domain.HistoryIntakeStatus.DEFICIENT;
import static com.foodon.foodon.recommend.domain.RecommendReasonFormat.*;
import static com.foodon.foodon.recommend.domain.RecommendReasonFormat.CARBS_DEFICIENT;
import static com.foodon.foodon.recommend.domain.RecommendReasonFormat.PROTEIN_DEFICIENT;
import static com.foodon.foodon.recommend.exception.RecommendFoodErrorCode.ILLEGAL_WEEK_RANGE;

@Service
@RequiredArgsConstructor
public class RecommendFoodService {

    private static final List<NutrientCode> NUTRIENT_CODES = Arrays.asList(
        NutrientCode.KCAL,
        NutrientCode.CARBS,
        NutrientCode.PROTEIN,
        NutrientCode.FAT
    );
	private static final BigDecimal MIN_GOAL_RATIO = BigDecimal.valueOf(0.15);
	private static final BigDecimal MAX_GOAL_RATIO = BigDecimal.valueOf(0.25);

    private final RecommendFoodRepository recommendFoodRepository;
    private final FoodRepository foodRepository;
    private final MemberRepository memberRepository;
    private final ActivityLevelRepository activityLevelRepository;
    private final MemberStatusRepository memberStatusRepository;
    private final IntakeLogRepository intakeLogRepository;
    private final NutrientPlanRepository nutrientPlanRepository;

    @Transactional(readOnly = true)
    public List<RecommendFoodResponse> getRecommendFoodsByWeek(
            YearMonth yearMonth,
            int week,
            Member member
    ){
        validateWeekInMonth(yearMonth, week);
        LocalDateTime start = getStartMondayOfWeek(yearMonth, week).atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        List<RecommendFood> recommendFoods = recommendFoodRepository.findByMemberAndCreatedAtBetween(member, start, end);
        Map<Long, List<NutrientClaimInfo>> nutrientClaimsByFoodId = getNutrientClaimsOfFoods(recommendFoods);

        return recommendFoods.stream()
                .map(recommendFood -> RecommendFoodResponse.from(
                        recommendFood,
                        nutrientClaimsByFoodId.getOrDefault(recommendFood.getFoodId(), List.of())
                )).toList();
    }

    private Map<Long, List<NutrientClaimInfo>> getNutrientClaimsOfFoods(
            List<RecommendFood> recommendFoods
    ) {
        List<Long> recommendFoodIds = recommendFoods.stream()
                .map(RecommendFood::getFoodId)
                .toList();

        List<NutrientClaimInfo> nutrientClaims = foodRepository.findNutrientClaimsByFoodIds(recommendFoodIds);
        return nutrientClaims.stream().collect(Collectors.groupingBy(NutrientClaimInfo::foodId));
    }

    private void validateWeekInMonth(YearMonth yearMonth, int week) {
        int firstDayOfWeekValue = yearMonth.atDay(1).getDayOfWeek().getValue(); // 1일 시작 요일
        int lastDay = yearMonth.lengthOfMonth();
        int maxWeek = (int) Math.ceil((double) (lastDay + firstDayOfWeekValue - 1) / 7.0);

        if(week < 1 || week > maxWeek) {
            throw new RecommendFoodBadRequestException(ILLEGAL_WEEK_RANGE);
        }
    }

    private LocalDate getStartMondayOfWeek(YearMonth yearMonth, int week) {
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate targetDay = firstDay.plusDays((week - 1) * 7L);

        return targetDay.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    @Transactional(readOnly = true)
    public List<Member> getRecentlyActiveMembers() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        return memberRepository.findAllActiveMembers(oneMonthAgo);
    }

    @Transactional(readOnly = true)
    public List<RecommendedFood> loadAllRecommendedFoods() {
        return foodRepository.findRecommendedFoodsWithNutrientInfo();
    }

    @Transactional
    public void generateWeeklyRecommendations(
        Member member,
        List<RecommendedFood> recommendedFoods,
        LocalDateTime time
    ) {
        // 지난 일주일 간의 섭취기록 가져오기
        LocalDate end = time.minusDays(1).toLocalDate();
        LocalDate start = end.minusDays(6);

        List<IntakeLog> intakeLogs = intakeLogRepository.findByMemberAndDateBetween(member, start, end);

        // 지난 주 식단 영양성분 섭취량 상태 판단
        MemberStatus latestStatus = getLatestStatusOrThrow(member);
        ActivityLevel activityLevel = findActivityLevelById(latestStatus.getActivityLevelId());
        NutrientPlan nutrientPlan = findNutrientPlanById(latestStatus.getNutrientPlanId());
        Map<NutrientCode, HistoryIntakeStatus> intakeStatusMap = evaluateHistoryIntakeStatus(intakeLogs, nutrientPlan);

        // 최소 권장량 ~ 최대 권장량 구하기
        BigDecimal goalKcal = NutrientGoal.calculateGoalKcal(member, latestStatus, activityLevel);
		NutrientGoal minimumNutrientGoal = NutrientGoal.from(goalKcal.multiply(MIN_GOAL_RATIO), nutrientPlan);
		NutrientGoal maximumNutrientGoal = NutrientGoal.from(goalKcal.multiply(MAX_GOAL_RATIO), nutrientPlan);

        // 조건에 일치하는 추천음식 찾기
        List<RecommendedFood> filtered = recommendedFoods.stream()
            .filter(food -> {
                for (NutrientCode code : NUTRIENT_CODES) {
                    NutrientInfo nutrientInfo = food.nutrients().get(code);
                    BigDecimal value = nutrientInfo == null ? BigDecimal.ZERO : nutrientInfo.value();
                    BigDecimal totalValue = BigDecimalUtil.divide(
                        BigDecimalUtil.multiply(value, food.servingSize()),
                        BigDecimal.valueOf(100)
                    );

                    HistoryIntakeStatus status = intakeStatusMap.getOrDefault(code, NORMAL);

                    // 탄단지 비율이 맞는지 확인
                    if (totalValue.compareTo(minimumNutrientGoal.getGoalKcal(code)) < 0
                        || totalValue.compareTo(maximumNutrientGoal.getGoalKcal(code)) > 0
                    ) {
                        return false;
                    }

                    if (status == DEFICIENT && totalValue.compareTo(minimumNutrientGoal.getGoalKcal(code)) < 0) {
                        return false;
                    }

                    if (status == EXCESSIVE && totalValue.compareTo(minimumNutrientGoal.getGoalKcal(code)) > 0) {
                        return false;
                    }
                }
                return true;
            })
            .collect(Collectors.toList());

        Collections.shuffle(filtered);

		filtered.stream()
			.limit(2)
			.map(food -> RecommendFood.from(
                    food, member, generateRecommendReason(food, intakeStatusMap, minimumNutrientGoal, maximumNutrientGoal), time
            )).forEach(recommendFoodRepository::save);
	}

    private String generateRecommendReason(
            RecommendedFood food,
            Map<NutrientCode, HistoryIntakeStatus> intakeStatusMap,
            NutrientGoal minimumNutrientGoal,
            NutrientGoal maximumNutrientGoal
    ) {
        for (NutrientCode code : NUTRIENT_CODES) {
            HistoryIntakeStatus intakeStatus = intakeStatusMap.getOrDefault(code, NORMAL);
            NutrientInfo nutrientInfo = food.nutrients().get(code);
            if (nutrientInfo == null) continue;

            // 1회 제공량 당 함량
            BigDecimal value = BigDecimalUtil.divide(
                    BigDecimalUtil.multiply(food.nutrients().get(code).value(), food.servingSize()),
                    BigDecimal.valueOf(100)
            );

            if(intakeStatus == DEFICIENT) {
                BigDecimal threshold = minimumNutrientGoal.getGoalKcal(code)
                        .multiply(BigDecimal.valueOf(0.9));

                if (value.compareTo(threshold) >= 0) { // 최소 권장량 이상으로 보충 가능함
                    return switch (code) {
                        case PROTEIN -> PROTEIN_DEFICIENT.getMessage();
                        case CARBS -> CARBS_DEFICIENT.getMessage();
                        case FAT -> FAT_DEFICIENT.getMessage();
                        case KCAL -> KCAL_DEFICIENT.getMessage();
                        default -> BALANCED.getMessage();
                    };
                }
            }

            if (intakeStatus == EXCESSIVE) {
                BigDecimal threshold = maximumNutrientGoal.getGoalKcal(code)
                        .multiply(BigDecimal.valueOf(0.9));;

                if (value.compareTo(threshold) < 0) {
                    return switch (code) {
                        case FAT -> LOW_FAT.getMessage();
                        case CARBS -> LOW_CARBS.getMessage();
                        case KCAL -> LOW_KCAL.getMessage();
                        default -> BALANCED.getMessage();
                    };
                }
            }
        }

        return BALANCED.getMessage();
    }

    /**
     * 부족한 날, 정상인 날, 과다한 날 빈도 수로 측정
     * 지난 주 유저의 식단 중 열량, 탄단지 섭취량 상태 체크
     */
    private Map<NutrientCode, HistoryIntakeStatus> evaluateHistoryIntakeStatus(
            List<IntakeLog> intakeLogs,
            NutrientPlan nutrientPlan
    ) {
        Map<NutrientCode, Integer> deficientDays = new EnumMap<>(NutrientCode.class);
        Map<NutrientCode, Integer> normalDays = new EnumMap<>(NutrientCode.class);
        Map<NutrientCode, Integer> excessiveDays = new EnumMap<>(NutrientCode.class);

        for (NutrientCode code : NutrientCode.values()) {
            deficientDays.put(code, 0);
            normalDays.put(code, 0);
            excessiveDays.put(code, 0);
        }

        for (IntakeLog log : intakeLogs) {
            Map<NutrientCode, BigDecimal> nutrientValueMap = getNutrientValueMap(log);
            NutrientGoal dailyGoal = NutrientGoal.from(log.getGoalKcal(), nutrientPlan);

            for (NutrientCode code : NutrientCode.values()) {
                BigDecimal intake = nutrientValueMap.getOrDefault(code, BigDecimal.ZERO);
                BigDecimal goal = dailyGoal.getGoalKcal(code);
                BigDecimal ratio = BigDecimalUtil.divide(intake, goal);

                if (ratio.compareTo(BigDecimal.valueOf(0.8)) < 0) {
                    deficientDays.compute(code, (k, count) -> count + 1);
                } else if (ratio.compareTo(BigDecimal.valueOf(1.1)) > 0) {
                    excessiveDays.compute(code, (k, count) -> count + 1);
                } else {
                    normalDays.compute(code, (k, count) -> count + 1);
                }
            }
        }

        // 부족, 정상, 과다한 날 빈도 수로 상태 평가
        Map<NutrientCode, HistoryIntakeStatus> result = new EnumMap<>(NutrientCode.class);
        for (NutrientCode code : NutrientCode.values()) {
            int deficient = deficientDays.get(code);
            int excessive = excessiveDays.get(code);

            HistoryIntakeStatus historyIntakeStatus = (excessive >= 4)
                    ? EXCESSIVE
                    : (deficient >= 4)
                    ? DEFICIENT : NORMAL;

            result.put(code, historyIntakeStatus);
        }

        return result;
    }

    private Map<NutrientCode, BigDecimal> getNutrientValueMap(IntakeLog intakeLog) {
        Map<NutrientCode, BigDecimal> nutrientMap = new EnumMap<>(NutrientCode.class);
        nutrientMap.put(NutrientCode.KCAL, intakeLog.getIntakeKcal());
        nutrientMap.put(NutrientCode.CARBS, intakeLog.getIntakeCarbs());
        nutrientMap.put(NutrientCode.PROTEIN, intakeLog.getIntakeProtein());
        nutrientMap.put(NutrientCode.FAT, intakeLog.getIntakeFat());
        return nutrientMap;
    }

    private ActivityLevel findActivityLevelById(Long activityLevelId) {
        return activityLevelRepository.findById(activityLevelId)
            .orElseThrow(
                () -> new NoSuchElementException(("해당 ID의 활동량 유형이 존재하지 않습니다. activityLevelId = " + activityLevelId)));
    }

    private NutrientPlan findNutrientPlanById(Long nutrientPlanId) {
        return nutrientPlanRepository.findById(nutrientPlanId)
            .orElseThrow(
                () -> new NoSuchElementException(("해당 ID의 관리 유형 유형이 존재하지 않습니다. nutrientPlanId = " + nutrientPlanId)));
    }

    private MemberStatus getLatestStatusOrThrow(Member member) {
        return memberStatusRepository.findTopByMemberIdOrderByCreatedAtDesc(member.getId())
            .orElseThrow(() -> new MemberException.MemberBadRequestException(MemberErrorCode.MEMBER_STATUS_NOT_FOUND));
    }

}

