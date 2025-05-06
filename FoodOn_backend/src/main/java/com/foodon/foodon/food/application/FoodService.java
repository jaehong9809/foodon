package com.foodon.foodon.food.application;

import com.foodon.foodon.food.domain.*;
import com.foodon.foodon.food.dto.CustomFoodCreateRequest;
import com.foodon.foodon.food.dto.FoodWithNutrientInfo;
import com.foodon.foodon.food.repository.FoodNutrientRepository;
import com.foodon.foodon.food.repository.FoodRepository;
import com.foodon.foodon.food.repository.NutrientRepository;
import com.foodon.foodon.meal.dto.NutrientProfile;
import com.foodon.foodon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.foodon.foodon.food.exception.FoodException.FoodBadRequestException;
import static com.foodon.foodon.food.exception.FoodErrorCode.ILLEGAL_NUTRIENT_ID;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final FoodNutrientRepository foodNutrientRepository;
    private final NutrientRepository nutrientRepository;

    @Transactional
    public long saveCustomFood(
            CustomFoodCreateRequest request,
            Member member
    ) {
        Food food = Food.createCustomFoodByMember(request, member);
        foodRepository.save(food);
        registerFoodNutrients(request.nutrients(), food);

        return food.getId();
    }

    private void registerFoodNutrients(
            NutrientProfile nutrients,
            Food food
    ) {
        List<Nutrient> nutrientList = nutrientRepository.findAll();
        Map<NutrientType, Long> nutrientIdMap = convertToTypeIdMap(nutrientList);

        nutrients.toMap().forEach((type, value) -> {
            if (value == null) {
                return;
            }

            FoodNutrient foodNutrient = FoodNutrient.createFoodNutrient(
                    food.getId(),
                    nutrientIdMap.get(type),
                    value
            );
            foodNutrientRepository.save(foodNutrient);
        });
    }

    private Map<NutrientType, Long> convertToTypeIdMap(List<Nutrient> nutrientList) {
        return nutrientList.stream()
                .collect(Collectors.toMap(
                        nutrient -> NutrientType.from(nutrient.getType()),
                        Nutrient::getId
                ));
    }

    public FoodWithNutrientInfo getFood(
            Long foodId,
            FoodType type,
            Member member
    ) {
        return foodRepository.findFoodInfoWithNutrientByIdAndType(foodId, type, member);
    }

}
