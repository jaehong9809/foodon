package com.foodon.foodon.food.application;

import com.foodon.foodon.food.domain.CustomFood;
import com.foodon.foodon.food.domain.Food;
import com.foodon.foodon.food.domain.FoodInfo;
import com.foodon.foodon.food.dto.CustomFoodCreateRequest;
import com.foodon.foodon.food.dto.FoodInfoResponse;
import com.foodon.foodon.food.exception.FoodErrorCode;
import com.foodon.foodon.food.exception.FoodException;
import com.foodon.foodon.food.exception.FoodException.FoodBadRequestException;
import com.foodon.foodon.food.exception.FoodException.FoodConflictException;
import com.foodon.foodon.food.exception.FoodException.FoodNotFoundException;
import com.foodon.foodon.food.repository.CustomFoodRepository;
import com.foodon.foodon.food.repository.FoodRepository;
import com.foodon.foodon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.foodon.foodon.food.exception.FoodErrorCode.*;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final CustomFoodRepository customFoodRepository;

    @Transactional
    public long saveCustomFood(
            CustomFoodCreateRequest request,
            Member member
    ) {
        validateDuplicateCustomFood(request.foodName(), member);
        CustomFood customFood = CustomFood.createCustomFoodOfMember(request, member);
        customFoodRepository.save(customFood);

        return customFood.getId();
    }

    private void validateDuplicateCustomFood(
            String foodName,
            Member member
    ) {
        if(customFoodRepository.existsByMemberAndName(member, foodName)) {
            throw new FoodConflictException(CONFLICT_CUSTOM_FOOD);
        }
    }

    public FoodInfoResponse getFood(
            Long foodId,
            String type,
            Member member
    ) {
        FoodInfo foodInfo = findFoodInfoByIdAndType(foodId, type, member);

        return FoodInfoResponse.from(foodInfo);
    }

    private FoodInfo findFoodInfoByIdAndType(
            Long foodId,
            String type,
            Member member
    ) {
        return switch (type.toLowerCase()) {
            case "public" -> findPublicFoodById(foodId);
            case "custom" -> findCustomFoodByIdAndMember(foodId, member);
            default -> throw new FoodBadRequestException(ILLEGAL_FOOD_TYPE);
        };
    }

    private Food findPublicFoodById(Long foodId) {
        return foodRepository.findById(foodId)
                .orElseThrow(() -> new FoodNotFoundException(NOT_FOUND_PUBLIC_FOOD));
    }

    private CustomFood findCustomFoodByIdAndMember(Long foodId, Member member) {
        return customFoodRepository.findByIdAndMember(foodId, member)
                .orElseThrow(() -> new FoodNotFoundException(NOT_FOUND_CUSTOM_FOOD));
    }

}
