package com.foodon.foodon.meal.application;

import com.foodon.foodon.food.domain.Food;
import com.foodon.foodon.food.repository.FoodRepository;
import com.foodon.foodon.image.application.S3ImageService;
import com.foodon.foodon.meal.dto.NutrientInfo;
import com.foodon.foodon.meal.infrastructure.DetectedFoodInfo;
import com.foodon.foodon.meal.infrastructure.MealDetectAiClient;
import com.foodon.foodon.meal.infrastructure.MealDetectAiResponse;
import com.foodon.foodon.meal.dto.MealInfoResponse;
import com.foodon.foodon.meal.dto.MealItemInfoResponse;
import com.foodon.foodon.meal.repository.MealItemRepository;
import com.foodon.foodon.meal.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.foodon.foodon.meal.util.NutrientCalculator.sum;
import static com.foodon.foodon.meal.util.NutrientCalculator.toRoundedInt;

@Service
@RequiredArgsConstructor
public class MealService {

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
        List<MealItemInfoResponse> mealItems = getMealItemInfo(detectedItems);
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

    private List<MealItemInfoResponse> getMealItemInfo(MealDetectAiResponse detectedItems) {
        Set<String> detectedFoodNames = extractFoodNameFrom(detectedItems);
        Set<Food> foods = foodRepository.findByNameIn(detectedFoodNames);
        Map<String, Food> foodMap = foods.stream()
                .collect(Collectors.toMap(Food::getName, Function.identity()));

        return detectedItems.foods().stream()
                .map(foodInfo -> toMealItemInfoResponse(foodMap, foodInfo))
                .collect(Collectors.toList());
    }

    private MealItemInfoResponse toMealItemInfoResponse(
            Map<String, Food> foodMap,
            DetectedFoodInfo foodInfo
    ) {

        Food food = findMatchedFood(foodMap, foodInfo.name());

        return MealItemInfoResponse.from(
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

}
