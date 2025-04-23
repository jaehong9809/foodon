package com.foodon.foodon.meal.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MealInfo {

    @Column(nullable = false)
    private int totalKcal;

    @Column(nullable = false)
    private int totalCarbs;

    @Column(nullable = false)
    private int totalProtein;

    @Column(nullable = false)
    private int totalFat;

}
