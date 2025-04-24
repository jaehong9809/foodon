package com.foodon.foodon.food.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Nutrient {

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal kcal;

    @Column(precision = 5, scale = 2)
    private BigDecimal carbs;

    @Column(precision = 5, scale = 2)
    private BigDecimal protein;

    @Column(precision = 5, scale = 2)
    private BigDecimal fat;

    @Column(precision = 5, scale = 2)
    private BigDecimal sugar;

    @Column(precision = 5, scale = 2)
    private BigDecimal calcium;

    @Column(precision = 5, scale = 2)
    private BigDecimal p;

    @Column(precision = 5, scale = 2)
    private BigDecimal sodium; // 나트륨

    @Column(precision = 5, scale = 2)
    private BigDecimal fiber;

    @Column(precision = 5, scale = 2)
    private BigDecimal saturatedFat;

    @Column(precision = 5, scale = 2)
    private BigDecimal unsaturatedFat;

    @Column(precision = 5, scale = 2)
    private BigDecimal transFat;

    @Column(precision = 5, scale = 2)
    private BigDecimal fattyAcid;

    @Column(precision = 5, scale = 2)
    private BigDecimal cholesterol;

    @Column(precision = 5, scale = 2)
    private BigDecimal potassium; // 칼륨

    @Column(precision = 5, scale = 2)
    private BigDecimal alcohol;

}
