package com.foodon.foodon.meal.application;

import com.foodon.foodon.common.util.NutrientCalculator;
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
import java.time.YearMonth;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.foodon.foodon.common.util.NutrientCalculator.*;
import static com.foodon.foodon.food.domain.FoodType.PUBLIC;
import static com.foodon.foodon.food.domain.NutrientCode.*;
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

    /**
     * 식단 이미지 업로드하여 AI 분석
     */
    public MealInfoResponse uploadAndDetect(MultipartFile multipartFile) {
        String imageFileName = localImageService.upload(multipartFile);
        MealDetectAiResponse detectedItems = mealDetectAiClient.detect(multipartFile);

        return convertToMealInfoResponse(imageFileName, detectedItems);
    }

    private MealInfoResponse convertToMealInfoResponse(
            String imageUrl,
            MealDetectAiResponse detectedItems
    ) {
        List<MealItemInfo> mealItems = getMealItemsInfoIn(detectedItems);

        return MealInfoResponse.from(
                imageUrl,
                sumTotalIntake(mealItems, NutrientProfile::kcal),
                sumTotalIntake(mealItems, NutrientProfile::carbs),
                sumTotalIntake(mealItems, NutrientProfile::protein),
                sumTotalIntake(mealItems, NutrientProfile::fat),
                mealItems
        );
    }

    private List<MealItemInfo> getMealItemsInfoIn(MealDetectAiResponse detectedItems) {
        Set<String> detectedFoodNames = extractFoodNameFrom(detectedItems);
        Map<String, FoodWithNutrientInfo> foodNameMap = getFoodNameMap(detectedFoodNames);

        return detectedItems.food().stream()
                .map(foodInfo -> convertToMealItemInfoResponse(foodNameMap, foodInfo))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Set<String> extractFoodNameFrom(MealDetectAiResponse detectedItems) {
        return detectedItems.food().stream()
                .map(DetectedFoodInfo::name)
                .collect(Collectors.toSet());
    }

    private Map<String, FoodWithNutrientInfo> getFoodNameMap(Set<String> detectedFoodNames) {
        List<FoodWithNutrientInfo> foods = foodRepository.findFoodInfoWithNutrientByNameIn(detectedFoodNames);
        return foods.stream()
                .collect(Collectors.toMap(FoodWithNutrientInfo::foodName, Function.identity()));
    }

    private MealItemInfo convertToMealItemInfoResponse(
            Map<String, FoodWithNutrientInfo> foodNameMap,
            DetectedFoodInfo foodInfo
    ) {
        FoodWithNutrientInfo matchedFood = foodNameMap.get(foodInfo.name());
        if(Objects.isNull(matchedFood)) {
            log.info("AI가 인식한 음식명 '{}' 이(가) DB에서 매칭되는 것이 없습니다.", foodInfo.name());
            return null;
        }

        return MealItemInfo.from(
                matchedFood,
                BigDecimal.valueOf(foodInfo.count()),
                foodInfo.positions(),
                createNutrientProfileOf(matchedFood)
        );
    }

    /**
     * [영양소타입 : 1회 제공 당 함량] 으로 매핑
     */
    private Map<NutrientCode, BigDecimal> createNutrientProfileOf(
            FoodWithNutrientInfo food
    ) {
        return food.nutrients().stream()
                .collect(Collectors.toMap(
                        NutrientInfo::code,
                        nutrient -> NutrientCalculator.calculateNutrientPerServing(food.servingSize(), nutrient.value())
                ));
    }

    /**
     * 식단 기록하기
     */
    public long saveMeal(
            MealCreateRequest request,
            Member member
    ) {
        String imageUrl = s3ImageService.upload(request.imageFileName());
        Map<FoodType, List<Long>> foodTypeMap = groupByTypeFromMealItemsInfos(request.mealItems());
        Map<Long, FoodWithNutrientInfo> foodMap = getFoodInfoInMealItems(foodTypeMap, member);

        Meal meal = createMealWithFoodInfo(foodMap, member, request, imageUrl);
        addMealItemsToMeal(member, meal, request.mealItems());
        mealRepository.save(meal);
        intakeLogService.saveIntakeLog(member, meal);

        return meal.getId();
    }

    private Map<FoodType, List<Long>> groupByTypeFromMealItemsInfos(
            List<MealItemInfo> mealItems
    ) {
        return mealItems.stream()
                .collect(Collectors.groupingBy(
                        MealItemInfo::type,
                        Collectors.mapping(MealItemInfo::foodId, Collectors.toList())
                ));
    }

    private Meal createMealWithFoodInfo(
            Map<Long, FoodWithNutrientInfo> foodMap,
            Member member,
            MealCreateRequest request,
            String imageUrl
    ) {
        Map<Long, BigDecimal> quantityMap = extractQuantityMap(request.mealItems());
        BigDecimal totalKcal = sumNutrientByCodeAndQuantity(foodMap, quantityMap, KCAL);
        BigDecimal totalCarbs = sumNutrientByCodeAndQuantity(foodMap, quantityMap, CARBS);
        BigDecimal totalProtein = sumNutrientByCodeAndQuantity(foodMap, quantityMap, PROTEIN);
        BigDecimal totalFat = sumNutrientByCodeAndQuantity(foodMap, quantityMap, FAT);

        return Meal.createMeal(
                member,
                imageUrl,
                totalKcal,
                totalCarbs,
                totalProtein,
                totalFat,
                request.mealTimeType(),
                request.mealTime()
        );
    }

    private Map<Long, BigDecimal> extractQuantityMap(
            List<MealItemInfo> items
    ) {
        return items.stream()
                .collect(Collectors.toMap(
                        MealItemInfo::foodId,
                        MealItemInfo::quantity
                ));
    }

    private void addMealItemsToMeal(
            Member member,
            Meal meal,
            List<MealItemInfo> mealItemInfos
    ) {
        if(Objects.isNull(mealItemInfos)) {
            return;
        }

        mealItemInfos.forEach(mealItemCreateInfo -> addItemToMeal(member, meal, mealItemCreateInfo));
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

    private boolean isThisWeekRecommendMealItem(
            Member member,
            MealItemInfo mealItemInfo
    ) {
        return recommendFoodRepository
                .existsThisWeekRecommend(member, mealItemInfo.type(), mealItemInfo.foodId());
    }

    private void addPositionsToMealItem(MealItem mealItem, List<PositionInfo> positionInfos) {
        positionInfos.forEach(positionInfo -> Position.createPosition(mealItem, positionInfo));
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

    /**
     * 식단 상세정보 조회
     */
    @Transactional(readOnly = true)
    public MealDetailInfoResponse getMealDetailInfo(
            Long mealId,
            Member member
    ){
        Meal meal = findMealByIdAndMember(member, mealId);
        List<MealItem> mealItems = meal.getMealItems();
        Map<FoodType, List<Long>> foodTypeMap = groupByTypeFromMealItems(mealItems);
        Map<Long, FoodWithNutrientInfo> foodMap = getFoodInfoInMealItems(foodTypeMap, member);
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

    private Map<FoodType, List<Long>> groupByTypeFromMealItems(
            List<MealItem> mealItems
    ) {
        return mealItems.stream()
                .collect(Collectors.groupingBy(
                        MealItem::getFoodType,
                        Collectors.mapping(MealItem::getFoodId, Collectors.toList())
                ));
    }

    public List<MealCalendarResponse> getRecommendMealRecords(
            YearMonth yearMonth,
            Member member
    ){
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime end = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        Map<LocalDate, List<MealThumbnailInfo>> mealMap = getMealByDateMap(member, start, end);

        return mealMap.entrySet().stream()
                .map(entry -> MealCalendarResponse.from(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(MealCalendarResponse::date))
                .toList();
    }

    private Map<LocalDate, List<MealThumbnailInfo>> getMealByDateMap(
            Member member,
            LocalDateTime start,
            LocalDateTime end
    ) {
        List<MealThumbnailInfo> meals = mealRepository.findRecommendMealsByMemberAndDate(member, start, end);
        Map<Long, MealThumbnailInfo> deduplicatedMealItemMap = deduplicateById(meals);

        return deduplicatedMealItemMap.values().stream()
                .collect(Collectors.groupingBy(meal -> meal.mealTime().toLocalDate()));
    }

    private Map<Long, MealThumbnailInfo> deduplicateById(
            List<MealThumbnailInfo> meals
    ) {
        return meals.stream()
                .collect(Collectors.toMap(
                        MealThumbnailInfo::mealItemId,
                        Function.identity(),
                        (prev, next) -> prev
                ));
    }

    private Map<Long, FoodWithNutrientInfo> getFoodInfoInMealItems(
            Map<FoodType, List<Long>> foodTypeMap,
            Member member
    ) {
        List<FoodWithNutrientInfo> publicFoods = getPublicFoodInfos(foodTypeMap);
        List<FoodWithNutrientInfo> customFoods = getCustomFoodInfos(foodTypeMap, member);

        return Stream.concat(publicFoods.stream(), customFoods.stream())
                .collect(Collectors.toMap(FoodWithNutrientInfo::foodId, Function.identity()));
    }

    private List<FoodWithNutrientInfo> getPublicFoodInfos(
            Map<FoodType, List<Long>> mealItemIdTypeMap
    ){
        List<Long> publicFoodIds = extractFoodIdsByType(mealItemIdTypeMap, List.of(PUBLIC));
        return publicFoodIds.isEmpty()
                ? List.of()
                : foodRepository.findAllBySearchCond(FoodSearchCond.of(publicFoodIds));
    }

    private List<FoodWithNutrientInfo> getCustomFoodInfos(
            Map<FoodType, List<Long>> mealItemIdTypeMap,
            Member member
    ){
        List<Long> customFoodIds = extractFoodIdsByType(mealItemIdTypeMap, FoodType.customTypes());
        return customFoodIds.isEmpty()
                ? List.of()
                : foodRepository.findAllBySearchCond(FoodSearchCond.of(customFoodIds, member));
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

        Map<NutrientCode, BigDecimal> nutrientMap = createNutrientProfileOf(food);
        return MealItemInfo.from(
                food,
                mealItem.getQuantity(),
                mealItem.getPositions().stream().map(PositionInfo::of).toList(),
                nutrientMap
        );
    }

    private List<Long> extractFoodIdsByType(
            Map<FoodType, List<Long>> mealItemIdTypeMap,
            List<FoodType> types
    ) {
        return types.stream()
                .flatMap(type -> mealItemIdTypeMap.getOrDefault(type, List.of()).stream())
                .toList();
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
        BigDecimal intakeKcal = nutrientIntakeMap.getOrDefault(KCAL, BigDecimal.ZERO);

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
