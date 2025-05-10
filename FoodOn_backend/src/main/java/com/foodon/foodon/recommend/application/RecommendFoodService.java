package com.foodon.foodon.recommend.application;

import com.foodon.foodon.common.util.NutrientCalculator;
import com.foodon.foodon.food.dto.FoodWithNutrientInfo;
import com.foodon.foodon.food.repository.FoodRepository;
import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.recommend.domain.RecommendFood;
import com.foodon.foodon.recommend.domain.nutrientclaims.NutrientClaim;
import com.foodon.foodon.recommend.domain.nutrientclaims.NutrientClaimEvaluator;
import com.foodon.foodon.recommend.domain.nutrientclaims.NutrientServingInfo;
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
import java.time.temporal.WeekFields;
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
        Map<Long, List<NutrientClaim>> nutrientClaimsByFoodId = getNutrientClaimsByFood(recommendFoods);

        return recommendFoods.stream()
                .map(recommendFood -> RecommendFoodResponse.from(
                        recommendFood,
                        nutrientClaimsByFoodId.get(recommendFood.getId())
                )).toList();
    }

    private void validateWeekInMonth(YearMonth yearMonth, int week) {
        int firstDayOfWeekValue = yearMonth.atDay(1).getDayOfWeek().getValue(); // 1일 시작 요일
        int lastDay = yearMonth.lengthOfMonth();
        int maxWeek = (int) Math.ceil((double) (lastDay + firstDayOfWeekValue - 1) / 7.0);

        if(week < 1 || week > maxWeek) {
            throw new RecommendFoodBadRequestException(ILLEGAL_WEEK_RANGE);
        }
    }

    private Map<Long, List<NutrientClaim>> getNutrientClaimsByFood(
            List<RecommendFood> recommendFoods
    ) {
        return recommendFoods.stream()
                .collect(Collectors.toMap(
                        RecommendFood::getId,
                        this::evaluateNutrientClaims
                ));
    }

    private List<NutrientClaim> evaluateNutrientClaims(RecommendFood recommendFood) {
        FoodWithNutrientInfo foodWithNutrientInfo = foodRepository.findFoodInfoWithNutrientByIdAndType(
                recommendFood.getFoodId(),
                recommendFood.getFoodType(),
                recommendFood.getMember()
        );

        List<NutrientServingInfo> nutrientServingInfos = convertToNutrientServingInfos(foodWithNutrientInfo);
        return NutrientClaimEvaluator.evaluate(nutrientServingInfos);
    }

    private List<NutrientServingInfo> convertToNutrientServingInfos(
            FoodWithNutrientInfo food
    ) {
        return food.nutrients().stream()
                .map(nutrient -> new NutrientServingInfo(
                        nutrient.code(),
                        nutrient.value(),
                        NutrientCalculator.calculateNutrientPerServing(food.servingSize(), nutrient.value())
                ))
                .toList();
    }

    private LocalDate getStartMondayOfWeek(YearMonth yearMonth, int week) {
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate targetDay = firstDay.plusDays((week - 1) * 7L);

        return targetDay.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

}

