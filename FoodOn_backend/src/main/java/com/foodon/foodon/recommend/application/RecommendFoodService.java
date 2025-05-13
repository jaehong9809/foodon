package com.foodon.foodon.recommend.application;

import com.foodon.foodon.common.util.BigDecimalUtil;
import com.foodon.foodon.common.util.NutrientGoal;
import com.foodon.foodon.food.domain.NutrientCode;
import com.foodon.foodon.food.dto.NutrientClaimInfo;
import com.foodon.foodon.food.dto.NutrientInfo;
import com.foodon.foodon.food.repository.FoodRepository;
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
import com.foodon.foodon.recommend.domain.RecommendFood;
import com.foodon.foodon.recommend.dto.RecommendFoodResponse;
import com.foodon.foodon.recommend.dto.RecommendedFood;
import com.foodon.foodon.recommend.exception.RecommendFoodException.RecommendFoodBadRequestException;
import com.foodon.foodon.recommend.repository.RecommendFoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
        MemberStatus latestStatus = getLatestStatusOrThrow(member);
        ActivityLevel activityLevel = findActivityLevelById(latestStatus.getActivityLevelId());
        BigDecimal goalKcal = NutrientGoal.calculateGoalKcal(member, latestStatus, activityLevel);
        NutrientPlan nutrientPlan = findNutrientPlanById(latestStatus.getNutrientPlanId());

		NutrientGoal minimumNutrientGoal = NutrientGoal.from(goalKcal.multiply(MIN_GOAL_RATIO), nutrientPlan);
		NutrientGoal maximumNutrientGoal = NutrientGoal.from(goalKcal.multiply(MAX_GOAL_RATIO), nutrientPlan);

        List<RecommendedFood> filtered = recommendedFoods.stream()
            .filter(food -> {
                for (NutrientCode code : NUTRIENT_CODES) {
                    NutrientInfo nutrientInfo = food.nutrients().get(code);
                    BigDecimal value = nutrientInfo == null ? BigDecimal.ZERO : nutrientInfo.value();
                    BigDecimal totalValuye = BigDecimalUtil.divide(
                        BigDecimalUtil.multiply(value, food.servingSize()),
                        BigDecimal.valueOf(100)
                    );
                    if (totalValuye.compareTo(minimumNutrientGoal.getGoalKcal(code)) < 0
                        || totalValuye.compareTo(maximumNutrientGoal.getGoalKcal(code)) > 0
                    ) {
                        return false;
                    }
                }
                return true;
            })
            .collect(Collectors.toList());

        Collections.shuffle(filtered);

		filtered.stream()
			.limit(2)
			.map(food -> RecommendFood.from(food, member, time))
			.forEach(recommendFoodRepository::save);
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

