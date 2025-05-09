package com.foodon.foodon.recommend.application;

import com.foodon.foodon.common.util.NutrientCalculator;
import com.foodon.foodon.food.dto.FoodWithNutrientInfo;
import com.foodon.foodon.food.repository.FoodRepository;
import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.recommend.domain.RecommendFood;
import com.foodon.foodon.recommend.domain.characteristic.FoodCharacteristic;
import com.foodon.foodon.recommend.domain.characteristic.FoodCharacteristicEvaluator;
import com.foodon.foodon.recommend.domain.characteristic.NutrientServingInfo;
import com.foodon.foodon.recommend.dto.RecommendFoodResponse;
import com.foodon.foodon.recommend.repository.RecommendFoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendFoodService {

    private final RecommendFoodRepository recommendFoodRepository;
    private final FoodRepository foodRepository;


    public List<RecommendFoodResponse> getRecommendFoodsByWeek(
            LocalDate date,
            Member member
    ){
        LocalDateTime start = getMondayOfWeek(date).atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        List<RecommendFood> recommendFoods = recommendFoodRepository.findByMemberAndCreatedAtBetween(member, start, end);
        Map<Long, List<FoodCharacteristic>> characteristicsById = getCharacteristicsByFood(recommendFoods);

        return recommendFoods.stream()
                .map(recommendFood -> RecommendFoodResponse.from(
                        recommendFood,
                        characteristicsById.get(recommendFood.getId())
                )).toList();
    }

    private Map<Long, List<FoodCharacteristic>> getCharacteristicsByFood(
            List<RecommendFood> recommendFoods
    ) {
        return recommendFoods.stream()
                .collect(Collectors.toMap(
                        RecommendFood::getId,
                        this::evaluateCharacteristics
                ));
    }

    private List<FoodCharacteristic> evaluateCharacteristics(RecommendFood recommendFood) {
        FoodWithNutrientInfo foodWithNutrientInfo = foodRepository.findFoodInfoWithNutrientByIdAndType(
                recommendFood.getFoodId(),
                recommendFood.getFoodType(),
                recommendFood.getMember()
        );

        List<NutrientServingInfo> nutrientServingInfos = convertToNutrientServingInfos(foodWithNutrientInfo);
        return FoodCharacteristicEvaluator.evaluate(nutrientServingInfos);
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


    private LocalDate getMondayOfWeek(LocalDate date) {
        return date.with(WeekFields.of(DayOfWeek.MONDAY, 1).dayOfWeek(), 1);
    }

}

