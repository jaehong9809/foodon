package com.foodon.foodon.food.dto;

import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class FoodSearchCond {
    private FoodType foodType;
    private List<Long> foodIds;
    private Member member;

    @Builder
    private FoodSearchCond(
            FoodType foodType,
            List<Long> foodIds,
            Member member
    ) {
        this.foodType = foodType;
        this.foodIds = foodIds;
        this.member = member;
    }

    public static FoodSearchCond create(List<Long> foodIds){
        return FoodSearchCond.builder()
                .foodIds(foodIds)
                .build();
    }

    public static FoodSearchCond create(
            List<Long> foodIds,
            Member member
    ){
        return FoodSearchCond.builder()
                .foodIds(foodIds)
                .member(member)
                .build();
    }
}
