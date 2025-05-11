package com.foodon.foodon.recommend.application;

import com.foodon.foodon.food.dto.NutrientClaimInfo;
import com.foodon.foodon.food.repository.FoodRepository;
import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.recommend.domain.RecommendFood;
import com.foodon.foodon.recommend.dto.RecommendFoodResponse;
import com.foodon.foodon.recommend.exception.RecommendFoodException.RecommendFoodBadRequestException;
import com.foodon.foodon.recommend.repository.RecommendFoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.foodon.foodon.recommend.exception.RecommendFoodErrorCode.ILLEGAL_WEEK_RANGE;

@Service
@RequiredArgsConstructor
public class RecommendFoodService {

    private final RecommendFoodRepository recommendFoodRepository;
    private final FoodRepository foodRepository;


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

}

