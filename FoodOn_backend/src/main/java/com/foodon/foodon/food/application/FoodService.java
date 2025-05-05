package com.foodon.foodon.food.application;

import com.foodon.foodon.food.domain.Food;
import com.foodon.foodon.food.domain.FoodNutrient;
import com.foodon.foodon.food.dto.CustomFoodCreateRequest;
import com.foodon.foodon.food.dto.FoodWithNutrientInfo;
import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.food.repository.FoodNutrientRepository;
import com.foodon.foodon.food.repository.FoodRepository;
import com.foodon.foodon.food.repository.NutrientRepository;
import com.foodon.foodon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        validateNutrientIds(request.nutrients());
        Food food = Food.createCustomFoodByMember(request, member);
        foodRepository.save(food);
        registerFoodNutrients(request.nutrients(), food);

        return food.getId();
    }

    private void registerFoodNutrients(
            List<CustomFoodCreateRequest.NutrientInfo> nutrients,
            Food food
    ) {
        for(CustomFoodCreateRequest.NutrientInfo nutrientInfo : nutrients) {
            FoodNutrient foodNutrient = FoodNutrient.createFoodNutrient(
                    food.getId(),
                    nutrientInfo.id(),
                    nutrientInfo.value()
            );
            foodNutrientRepository.save(foodNutrient);
        }
    }

    private void validateNutrientIds(
            List<CustomFoodCreateRequest.NutrientInfo> nutrients
    ) {
        List<Long> nutrientIds = nutrients.stream()
                .map(CustomFoodCreateRequest.NutrientInfo::id).distinct().toList();

        if(nutrientRepository.findExistingIds(nutrientIds).size() != nutrientIds.size()) {
            throw new FoodBadRequestException(ILLEGAL_NUTRIENT_ID);
        }
    }

    public FoodWithNutrientInfo getFood(
            Long foodId,
            FoodType type,
            Member member
    ) {
        return foodRepository.findFoodInfoWithNutrientByIdAndType(foodId, type, member);
    }

}
