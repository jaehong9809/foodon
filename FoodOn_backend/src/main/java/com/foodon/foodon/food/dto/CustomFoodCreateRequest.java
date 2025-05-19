package com.foodon.foodon.food.dto;

import com.foodon.foodon.food.domain.Unit;
import com.foodon.foodon.meal.dto.NutrientProfile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CustomFoodCreateRequest(
        @NotBlank
        String foodName,

        @NotNull
        Unit unit,

        BigDecimal servingSize, // 중량

        NutrientProfile nutrients
) {
}
