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

import com.foodon.foodon.meal.domain.ManageNutrient;

import com.foodon.foodon.meal.domain.Meal;
import com.foodon.foodon.meal.domain.MealItem;
import com.foodon.foodon.meal.domain.Position;
import com.foodon.foodon.meal.dto.*;
import com.foodon.foodon.meal.exception.MealException;
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

import static com.foodon.foodon.common.util.BigDecimalUtil.toRoundedInt;
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


    public MealDetailInfoResponse getMealDetailInfo(
            Long mealId,
            Member member
    ){
        // Meal 정보 조회
        Meal meal = findMealByIdAndMember(member, mealId);

        // MealItems 정보 조회
        List<MealItem> mealItems = meal.getMealItems();

        // MealItem 조회, 타입으로 그룹핑
        Map<FoodType, List<MealItem>> mealItemTypeMap = mealItems.stream()
                .collect(Collectors.groupingBy(MealItem::getFoodType));

        // 공공데이터 음식 정보 조회
        List<Long> publicFoodIds = mealItemTypeMap.getOrDefault(FoodType.PUBLIC, List.of()).stream()
                .map(MealItem::getFoodId)
                .toList();
        List<FoodWithNutrientInfo> publicFoods = new ArrayList<>();
        if(!publicFoodIds.isEmpty()) {
            publicFoods.addAll(foodRepository.findAllBySearchCond(FoodSearchCond.create(publicFoodIds)));
        }

        // 커스텀 음식 조회 (memberId 포함)
        List<Long> customFoodIds = Stream.of(
                        mealItemTypeMap.getOrDefault(FoodType.CUSTOM, List.of()),
                        mealItemTypeMap.getOrDefault(FoodType.CUSTOM_MODIFIED, List.of())
                )
                .flatMap(List::stream)
                .map(MealItem::getFoodId)
                .toList();
        List<FoodWithNutrientInfo> customFoods = new ArrayList<>();
        if(!customFoodIds.isEmpty()) {
            customFoods.addAll(foodRepository.findAllBySearchCond(
                    FoodSearchCond.create(customFoodIds, member)));
        }

        List<FoodWithNutrientInfo> totalFoods = Stream.concat(
                publicFoods.stream(),
                customFoods.stream()
        ).toList();

        // 음식 ID 별로 묶음
        Map<Long, FoodWithNutrientInfo> totalFoodMap = totalFoods.stream()
                .collect(Collectors.toMap(FoodWithNutrientInfo::foodId, Function.identity()));

        // 음식 ID 별로 mealItem 묶음
        Map<Long, List<MealItem>> grouped = mealItems.stream()
                .collect(Collectors.groupingBy(MealItem::getFoodId));

        List<MealItemInfo> mealItemInfos = grouped.entrySet().stream()
                .map(entry -> {
                    Long foodId = entry.getKey();
                    List<MealItem> items = entry.getValue();
                    FoodWithNutrientInfo food = totalFoodMap.get(foodId);
                    if (food == null) return null;

                    BigDecimal totalQuantity = items.stream()
                            .map(MealItem::getQuantity)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    List<Position> positions = items.stream()
                            .map(MealItem::getPosition)
                            .filter(Objects::nonNull)
                            .toList();

                    return MealItemInfo.from(
                            food,
                            totalQuantity,
                            positions.stream()
                                    .map(PositionInfo::of)
                                    .toList(),
                            convertToTypedValueMap(food.nutrients())
                    );
                })
                .filter(Objects::nonNull)
                .toList();

        BigDecimal totalKcal = sumTotalIntake(mealItemInfos, NutrientProfile::kcal);
        BigDecimal totalCarbs = sumTotalIntake(mealItemInfos, NutrientProfile::carbs);
        BigDecimal totalProtein = sumTotalIntake(mealItemInfos, NutrientProfile::protein);
        BigDecimal totalFat = sumTotalIntake(mealItemInfos, NutrientProfile::fat);

        return MealDetailInfoResponse.from(
                meal,
                totalKcal,
                totalCarbs,
                totalProtein,
                totalFat,
                mealItemInfos
        );
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
