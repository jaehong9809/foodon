package com.foodon.foodon.food.repository;

import com.foodon.foodon.food.dto.FoodWithNutrientInfo;
import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.member.domain.Member;

import java.util.List;
import java.util.Set;

public interface FoodRepositoryCustom {
    FoodWithNutrientInfo findFoodInfoWithNutrientByIdAndType(
            Long foodId,
            FoodType type,
            Member member
    );

    List<FoodWithNutrientInfo> findFoodInfoWithNutrientByNameIn(
            Set<String> foodNames
    );


}
