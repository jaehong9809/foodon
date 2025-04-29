package com.foodon.foodon.food.domain;

import java.math.BigDecimal;

public interface FoodInfo {
    Long getId();
    String getName();
    FoodType getFoodType();
    BigDecimal getServingSize();
    Unit getUnit();
    Nutrient getNutrient();
}
