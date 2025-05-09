package com.foodon.foodon.meal.application;

import com.foodon.foodon.common.util.NutrientCalculator;
import com.foodon.foodon.food.domain.NutrientCode;
import com.foodon.foodon.food.dto.FoodWithNutrientInfo;
import com.foodon.foodon.food.dto.NutrientInfo;
import com.foodon.foodon.food.repository.FoodRepository;
import com.foodon.foodon.image.application.LocalImageService;
import com.foodon.foodon.image.application.S3ImageService;
import com.foodon.foodon.meal.domain.Meal;
import com.foodon.foodon.meal.domain.MealItem;
import com.foodon.foodon.meal.domain.Position;
import com.foodon.foodon.meal.dto.*;
import com.foodon.foodon.meal.exception.MealException.MealBadRequestException;
import com.foodon.foodon.meal.infrastructure.DetectedFoodInfo;
import com.foodon.foodon.meal.infrastructure.MealDetectAiClient;
import com.foodon.foodon.meal.infrastructure.MealDetectAiResponse;
import com.foodon.foodon.meal.repository.MealItemRepository;
import com.foodon.foodon.meal.repository.MealRepository;
import com.foodon.foodon.member.domain.Member;
import com.foodon.foodon.recommend.repository.RecommendFoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.foodon.foodon.common.util.BigDecimalUtil.toRoundedInt;
import static com.foodon.foodon.common.util.NutrientCalculator.sumTotalIntake;
import static com.foodon.foodon.meal.exception.MealErrorCode.MEAL_ITEM_IS_NULL;

@Slf4j
@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;
    private final MealItemRepository mealItemRepository;
    private final RecommendFoodRepository recommendFoodRepository;
    private final FoodRepository foodRepository;
    private final MealDetectAiClient mealDetectAiClient;
    private final S3ImageService s3ImageService;
    private final LocalImageService localImageService;


    public MealInfoResponse uploadAndDetect(MultipartFile multipartFile) {
        String imageFileName = localImageService.upload(multipartFile);
        MealDetectAiResponse detectedItems = mealDetectAiClient.detect(multipartFile);

        return convertToMealInfoResponse(imageFileName, detectedItems);
    }

    private MealInfoResponse convertToMealInfoResponse(
            String imageUrl,
            MealDetectAiResponse detectedItems
    ) {
        List<MealItemInfo> mealItems = getMealItemsInfoList(detectedItems);
        int totalKcal = toRoundedInt(sumTotalIntake(mealItems, NutrientProfile::kcal));
        int totalCarbs = toRoundedInt(sumTotalIntake(mealItems, NutrientProfile::carbs));
        int totalProtein = toRoundedInt(sumTotalIntake(mealItems, NutrientProfile::protein));
        int totalFat = toRoundedInt(sumTotalIntake(mealItems, NutrientProfile::fat));

        return MealInfoResponse.from(
                imageUrl,
                totalKcal,
                totalCarbs,
                totalProtein,
                totalFat,
                mealItems
        );
    }

    private List<MealItemInfo> getMealItemsInfoList(MealDetectAiResponse detectedItems) {
        Set<String> detectedFoodNames = extractFoodNameFrom(detectedItems);
        Map<String, FoodWithNutrientInfo> foodMap = mapToFoodByName(detectedFoodNames);

        return detectedItems.food().stream()
                .map(foodInfo -> convertToMealItemInfoResponse(foodMap, foodInfo))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Map<String, FoodWithNutrientInfo> mapToFoodByName(Set<String> detectedFoodNames) {
        List<FoodWithNutrientInfo> foods = foodRepository.findFoodInfoWithNutrientByNameIn(detectedFoodNames);
        return foods.stream()
                .collect(Collectors.toMap(FoodWithNutrientInfo::foodName, Function.identity()));
    }

    private MealItemInfo convertToMealItemInfoResponse(
            Map<String, FoodWithNutrientInfo> foodMap,
            DetectedFoodInfo foodInfo
    ) {
        FoodWithNutrientInfo matchedFood = foodMap.get(foodInfo.name());
        if(Objects.isNull(matchedFood)) {
            log.info("AI가 인식한 음식명 '{}' 이(가) DB에서 매칭되는 것이 없습니다.", foodInfo.name());
            return null;
        }

        return MealItemInfo.from(
                matchedFood,
                BigDecimal.valueOf(foodInfo.count()),
                foodInfo.positions(),
                convertToTypedValueMap(matchedFood.nutrients())
        );
    }

    /**
     * [영양소타입 : 값] 으로 매핑
     */
    private Map<NutrientCode, BigDecimal> convertToTypedValueMap(List<NutrientInfo> nutrients) {
        return nutrients.stream()
                .collect(Collectors.toMap(
                        NutrientInfo::code,
                        info -> NutrientCalculator.convertToMilligram(info.value(), info.nutrientUnit())
                ));
    }

    public Set<String> extractFoodNameFrom(MealDetectAiResponse detectedItems) {
        return detectedItems.food().stream()
                .map(DetectedFoodInfo::name)
                .collect(Collectors.toSet());
    }

    @Transactional
    public long saveMeal(
            MealCreateRequest request,
            Member member
    ) {
        String imageUrl = s3ImageService.upload(request.imageFileName());
        Meal meal = Meal.createMeal(member, imageUrl, request);
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
        boolean isRecommended = isThisWeekRecommendMealItem(member, mealItemInfo);
        positions.forEach(positionInfo -> MealItem.createMealItem(
                meal,
                mealItemInfo,
                Position.of(positionInfo),
                isRecommended
        ));
    }

    private boolean isThisWeekRecommendMealItem(
            Member member,
            MealItemInfo mealItemInfo
    ) {
        return recommendFoodRepository
                .existsThisWeekRecommend(member, mealItemInfo.type(), mealItemInfo.foodId());
    }

    @Transactional(readOnly = true)
    public List<MealSummaryResponse> getMealSummariesByDate(
            LocalDate date,
            Member member
    ) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        List<Meal> meals = mealRepository.findByMemberAndMealTimeBetween(member, startOfDay, endOfDay);

        return meals.stream().map(MealSummaryResponse::of).toList();
    }

}
