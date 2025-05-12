package com.foodon.foodon.food.application;

import com.foodon.foodon.common.util.NutrientCalculator;
import com.foodon.foodon.food.domain.*;
import com.foodon.foodon.food.dto.CustomFoodCreateRequest;
import com.foodon.foodon.food.dto.FoodDetailInfoResponse;
import com.foodon.foodon.food.dto.FoodWithNutrientInfo;
import com.foodon.foodon.food.dto.NutrientInfo;
import com.foodon.foodon.food.exception.FoodException.FoodConflictException;
import com.foodon.foodon.food.repository.FoodNutrientRepository;
import com.foodon.foodon.food.repository.FoodRepository;
import com.foodon.foodon.food.repository.NutrientRepository;
import com.foodon.foodon.meal.dto.NutrientProfile;
import com.foodon.foodon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.foodon.foodon.food.exception.FoodErrorCode.CONFLICT_CUSTOM_FOOD;

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
        checkDuplicateCustomFood(request.foodName(), member);
        Food food = Food.createCustomFoodByMember(request, member);
        foodRepository.save(food);
        registerFoodNutrients(request.nutrients(), food);

        return food.getId();
    }

    private void checkDuplicateCustomFood(
            String foodName,
            Member member
    ) {
        if(foodRepository.existsByMemberIdAndName(member.getId(), foodName)) {
            throw new FoodConflictException(CONFLICT_CUSTOM_FOOD);
        }
    }

    private void registerFoodNutrients(
            NutrientProfile nutrients,
            Food food
    ) {
        List<Nutrient> nutrientList = nutrientRepository.findAll();
        Map<NutrientCode, Long> nutrientCodeIdMap = getNutrientCodeIdMap(nutrientList);

        nutrients.toMap().forEach((code, value) -> {
            if (value == null) {
                return;
            }

            FoodNutrient foodNutrient = FoodNutrient.createFoodNutrient(
                    food.getId(),
                    nutrientCodeIdMap.get(code),
                    value
            );
            foodNutrientRepository.save(foodNutrient);
        });
    }

    private Map<NutrientCode, Long> getNutrientCodeIdMap(List<Nutrient> nutrientList) {
        return nutrientList.stream()
                .collect(Collectors.toMap(
                        Nutrient::getCode,
                        Nutrient::getId
                ));
    }

    public FoodDetailInfoResponse getFood(
            Long foodId,
            FoodType type,
            Member member
    ) {
        FoodWithNutrientInfo foodInfo = foodRepository.findFoodInfoWithNutrientByIdAndType(foodId, type, member);
        return FoodDetailInfoResponse.from(foodInfo, getNutrientValueMap(foodInfo));
    }

    private Map<NutrientCode, BigDecimal> getNutrientValueMap(
            FoodWithNutrientInfo foodInfo
    ) {
        return foodInfo.nutrients().stream()
                .collect(Collectors.toMap(
                        NutrientInfo::code,
                        NutrientInfo::value
                        // 1회 제공량 함량으로 변환해서 주도록 수정하겠습니다. (변환 로직이 다른 PR 에 존재)
                ));
    }

}
