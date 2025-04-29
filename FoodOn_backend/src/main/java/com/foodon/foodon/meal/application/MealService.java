package com.foodon.foodon.meal.application;

import com.foodon.foodon.food.domain.Food;
import com.foodon.foodon.food.repository.FoodRepository;
import com.foodon.foodon.image.application.S3ImageService;
import com.foodon.foodon.meal.domain.Meal;
import com.foodon.foodon.meal.domain.MealItem;
import com.foodon.foodon.meal.domain.Position;
import com.foodon.foodon.meal.dto.*;
import com.foodon.foodon.meal.exception.MealErrorCode;
import com.foodon.foodon.meal.exception.MealException;
import com.foodon.foodon.meal.exception.MealException.MealBadRequestException;
import com.foodon.foodon.meal.infrastructure.DetectedFoodInfo;
import com.foodon.foodon.meal.infrastructure.MealDetectAiClient;
import com.foodon.foodon.meal.infrastructure.MealDetectAiResponse;
import com.foodon.foodon.meal.repository.MealItemRepository;
import com.foodon.foodon.meal.repository.MealRepository;
import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.recommend.domain.RecommendFood;
import com.foodon.foodon.recommend.repository.RecommendFoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.foodon.foodon.common.util.BigDecimalUtil.toRoundedInt;
import static com.foodon.foodon.common.util.NutrientCalculator.sum;
import static com.foodon.foodon.meal.exception.MealErrorCode.MEAL_ITEM_IS_NULL;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;
    private final MealItemRepository mealItemRepository;
    private final RecommendFoodRepository recommendFoodRepository;
    private final FoodRepository foodRepository;
    private final MealDetectAiClient mealDetectAiClient;
    private final S3ImageService s3ImageService;


    public MealInfoResponse uploadAndDetect(MultipartFile multipartFile) {
        String imageUrl = s3ImageService.upload(multipartFile);
        MealDetectAiResponse detectedItems = mealDetectAiClient.detect(imageUrl);

        return convertToMealInfoResponse(imageUrl, detectedItems);
    }

    private MealInfoResponse convertToMealInfoResponse(
            String imageUrl,
            MealDetectAiResponse detectedItems
    ) {
        List<MealItemInfo> mealItems = getMealItemInfo(detectedItems);
        int totalKcal = toRoundedInt(sum(mealItems, NutrientInfo::kcal));
        int totalCarbs = toRoundedInt(sum(mealItems, NutrientInfo::carbs));
        int totalProtein = toRoundedInt(sum(mealItems, NutrientInfo::protein));
        int totalFat = toRoundedInt(sum(mealItems, NutrientInfo::fat));

        return MealInfoResponse.from(
                imageUrl,
                totalKcal,
                totalCarbs,
                totalProtein,
                totalFat,
                mealItems
        );
    }

    private List<MealItemInfo> getMealItemInfo(MealDetectAiResponse detectedItems) {
        Set<String> detectedFoodNames = extractFoodNameFrom(detectedItems);
        Set<Food> foods = foodRepository.findByNameIn(detectedFoodNames);
        Map<String, Food> foodMap = foods.stream()
                .collect(Collectors.toMap(Food::getName, Function.identity()));

        return detectedItems.foods().stream()
                .map(foodInfo -> toMealItemInfoResponse(foodMap, foodInfo))
                .collect(Collectors.toList());
    }

    private MealItemInfo toMealItemInfoResponse(
            Map<String, Food> foodMap,
            DetectedFoodInfo foodInfo
    ) {
        Food food = findMatchedFood(foodMap, foodInfo.name());

        return MealItemInfo.from(
                food,
                BigDecimal.valueOf(foodInfo.count()),
                foodInfo.positions()
        );
    }

    private Food findMatchedFood(
            Map<String, Food> foodMap,
            String foodName
    ) {
        return Optional.ofNullable(foodMap.get(foodName))
                .orElseThrow(() -> new NoSuchElementException("해당 이름의 음식이 존재하지 않습니다. name = " + foodName));
    }

    public Set<String> extractFoodNameFrom(MealDetectAiResponse detectedItems) {
        return detectedItems.foods().stream()
                .map(DetectedFoodInfo::name)
                .collect(Collectors.toSet());
    }

    @Transactional
    public long saveMeal(
            MealCreateRequest request,
            Member member
    ) {
        Meal meal = Meal.createMeal(member, request);
        addMealItemsToMeal(member, meal, request.mealItems());
        mealRepository.save(meal);

        return meal.getId();
    }

    private void addMealItemsToMeal(
            Member member,
            Meal meal,
            List<MealItemInfo> mealItemInfos
    ) {
        if(Objects.isNull(mealItemInfos)) {
            return;
        }

        mealItemInfos.forEach(mealItemInfo -> addItemToMeal(member, meal, mealItemInfo));
    }

    private void addItemToMeal(
            Member member,
            Meal meal,
            MealItemInfo mealItemInfo
    ) {
        if(Objects.isNull(mealItemInfo)) {
            throw new MealBadRequestException(MEAL_ITEM_IS_NULL);
        }

        List<PositionInfo> positions = mealItemInfo.positions();
        boolean isRecommended = isRecommendMealItem(member, mealItemInfo);
        positions.forEach(positionInfo -> MealItem.createMealItem(
                meal,
                mealItemInfo,
                Position.of(positionInfo),
                isRecommended
        ));
    }

    private boolean isRecommendMealItem(
            Member member,
            MealItemInfo mealItemInfo
    ) {
        return recommendFoodRepository
                .findByMemberAndFoodTypeAndFoodId(member, mealItemInfo.type(), mealItemInfo.foodId())
                .isPresent();
    }

}
