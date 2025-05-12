package com.foodon.foodon.meal.application;

import com.foodon.foodon.food.domain.*;
import com.foodon.foodon.food.dto.FoodSearchCond;
import com.foodon.foodon.food.dto.FoodWithNutrientInfo;
import com.foodon.foodon.food.dto.NutrientInfo;
import com.foodon.foodon.food.repository.FoodRepository;
import com.foodon.foodon.food.repository.NutrientRepository;
import com.foodon.foodon.image.application.LocalImageService;
import com.foodon.foodon.image.application.S3ImageService;
import com.foodon.foodon.intakelog.application.IntakeLogService;

import com.foodon.foodon.meal.domain.*;

import com.foodon.foodon.meal.dto.*;
import com.foodon.foodon.meal.exception.MealException.MealBadRequestException;
import com.foodon.foodon.meal.exception.MealException.MealNotFoundException;
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
import java.util.stream.Stream;

import static com.foodon.foodon.common.util.NutrientCalculator.sumTotalIntake;
import static com.foodon.foodon.meal.exception.MealErrorCode.MEAL_ITEM_IS_NULL;
import static com.foodon.foodon.meal.exception.MealErrorCode.NOT_FOUND_MEAL;

@Slf4j
@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;
    private final MealItemRepository mealItemRepository;
    private final RecommendFoodRepository recommendFoodRepository;
    private final FoodRepository foodRepository;
    private final NutrientRepository nutrientRepository;
    private final MealDetectAiClient mealDetectAiClient;
    private final S3ImageService s3ImageService;
    private final LocalImageService localImageService;
    private final IntakeLogService intakeLogService;


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
        BigDecimal totalKcal = sumTotalIntake(mealItems, NutrientProfile::kcal);
        BigDecimal totalCarbs = sumTotalIntake(mealItems, NutrientProfile::carbs);
        BigDecimal totalProtein = sumTotalIntake(mealItems, NutrientProfile::protein);
        BigDecimal totalFat = sumTotalIntake(mealItems, NutrientProfile::fat);

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
                        NutrientInfo::value
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
        intakeLogService.saveIntakeLog(member, meal);

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

        boolean isRecommended = isThisWeekRecommendMealItem(member, mealItemInfo);
        MealItem mealItem = MealItem.createMealItem(meal, mealItemInfo, isRecommended);
        addPositionsToMealItem(mealItem, mealItemInfo.positions());
    }

    private void addPositionsToMealItem(MealItem mealItem, List<PositionInfo> positionInfos) {
        positionInfos.forEach(positionInfo -> Position.createPosition(mealItem, positionInfo));
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

    @Transactional(readOnly = true)
    public MealDetailInfoResponse getMealDetailInfo(
            Long mealId,
            Member member
    ){
        Meal meal = findMealByIdAndMember(member, mealId);
        List<MealItem> mealItems = meal.getMealItems();
        Map<Long, FoodWithNutrientInfo> foodMap = getFoodInfoInMeal(mealItems, member);

        List<MealItemInfo> mealItemInfos = createMealItemInfos(mealItems, foodMap);
        return MealDetailInfoResponse.from(
                meal,
                sumTotalIntake(mealItemInfos, NutrientProfile::kcal),
                sumTotalIntake(mealItemInfos, NutrientProfile::carbs),
                sumTotalIntake(mealItemInfos, NutrientProfile::protein),
                sumTotalIntake(mealItemInfos, NutrientProfile::fat),
                mealItemInfos
        );
    }

    private List<MealItemInfo> createMealItemInfos(
            List<MealItem> mealItems,
            Map<Long, FoodWithNutrientInfo> foodMap
    ) {
        return mealItems.stream()
                .map(mealItem -> createMealItemInfo(mealItem, foodMap.get(mealItem.getFoodId())))
                .toList();
    }

    private MealItemInfo createMealItemInfo(
            MealItem mealItem,
            FoodWithNutrientInfo food
    ) {
        if(Objects.isNull(food)){
            throw new NoSuchElementException("해당 ID로 조회한 음식이 존재하지 않습니다, foodId = " + mealItem.getFoodId());
        }

        Map<NutrientCode, BigDecimal> nutrientMap = convertToTypedValueMap(food.nutrients());
        return MealItemInfo.from(
                food,
                mealItem.getQuantity(),
                mealItem.getPositions().stream().map(PositionInfo::of).toList(),
                nutrientMap
        );
    }

    private Map<Long, FoodWithNutrientInfo> getFoodInfoInMeal(
            List<MealItem> mealItems,
            Member member
    ) {
        Map<FoodType, List<MealItem>> mealItemTypeMap = groupMealItemsByType(mealItems);
        List<FoodWithNutrientInfo> publicFoods = getPublicFoodInfos(mealItemTypeMap);
        List<FoodWithNutrientInfo> customFoods = getCustomFoodInfos(mealItemTypeMap, member);

        return Stream.concat(publicFoods.stream(), customFoods.stream())
                .collect(Collectors.toMap(FoodWithNutrientInfo::foodId, Function.identity()));
    }

    private List<FoodWithNutrientInfo> getPublicFoodInfos(
            Map<FoodType, List<MealItem>> mealItemTypeMap
    ){
        List<Long> publicFoodIds = extractFoodIdsByType(mealItemTypeMap, List.of(FoodType.PUBLIC));
        return publicFoodIds.isEmpty()
                ? List.of()
                : foodRepository.findAllBySearchCond(FoodSearchCond.of(publicFoodIds));
    }

    private List<FoodWithNutrientInfo> getCustomFoodInfos(
            Map<FoodType, List<MealItem>> mealItemTypeMap,
            Member member
    ){
        List<Long> customFoodIds = extractFoodIdsByType(mealItemTypeMap, FoodType.customTypes());
        return customFoodIds.isEmpty()
                ? List.of()
                : foodRepository.findAllBySearchCond(FoodSearchCond.of(customFoodIds, member));
    }

    private List<Long> extractFoodIdsByType(
            Map<FoodType, List<MealItem>> mealItemTypeMap,
            List<FoodType> types
    ) {
        return types.stream()
                .flatMap(type -> mealItemTypeMap.getOrDefault(type, List.of()).stream())
                .map(MealItem::getFoodId)
                .toList();
    }

    private Map<FoodType, List<MealItem>> groupMealItemsByType(List<MealItem> mealItems) {
        return mealItems.stream().collect(Collectors.groupingBy(MealItem::getFoodType));
    }

    private Meal findMealByIdAndMember(Member member, Long mealId) {
        return mealRepository.findByMealIdFetchWithMealItems(member, mealId)
                .orElseThrow(() -> new MealNotFoundException(NOT_FOUND_MEAL));
    }

    public List<ManageNutrientResponse> getManageNutrientsByDate(
            LocalDate date,
            Member member
    ){
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        Map<NutrientCode, BigDecimal> nutrientIntakeMap = getNutrientIntakeMapByDate(member, startOfDay, endOfDay);
        List<Nutrient> restrictedNutrients = nutrientRepository.findByRestrictionTypeIsNot(RestrictionType.NONE);

        return convertToManageNutrientResponse(nutrientIntakeMap, restrictedNutrients);
    }

    private Map<NutrientCode, BigDecimal> getNutrientIntakeMapByDate(
            Member member,
            LocalDateTime start,
            LocalDateTime end
    ){
        return mealRepository.findNutrientIntakeByMemberAndDate(member, start, end).stream()
                .collect(Collectors.toMap(
                        NutrientIntakeInfo::nutrientCode,
                        NutrientIntakeInfo::intake
                ));
    }

    private List<ManageNutrientResponse> convertToManageNutrientResponse(
            Map<NutrientCode, BigDecimal> nutrientIntakeMap,
            List<Nutrient> restrictedNutrients
    ) {
        BigDecimal intakeKcal = nutrientIntakeMap.getOrDefault(NutrientCode.KCAL, BigDecimal.ZERO);

        return restrictedNutrients.stream()
                .map(nutrient -> {
                    BigDecimal intake = nutrientIntakeMap.getOrDefault(nutrient.getCode(), BigDecimal.ZERO);
                    ManageNutrient manageNutrient = ManageNutrient.from(nutrient, intakeKcal);
                    ManageStatus status = ManageStatus.evaluate(intake.doubleValue(), manageNutrient.getMin(), manageNutrient.getMax());

                    return ManageNutrientResponse.from(
                            nutrient,
                            intake,
                            BigDecimal.valueOf(manageNutrient.getMin()),
                            BigDecimal.valueOf(manageNutrient.getMax()),
                            status
                    );
                })
                .toList();
    }

}
