package com.foodon.foodon.food.domain;

import com.foodon.foodon.meal.dto.NutrientInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@Builder
@AllArgsConstructor(access = PRIVATE)
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

    public static Nutrient of(
        NutrientInfo nutrientInfo
    ) {
        return Nutrient.builder()
                .kcal(nutrientInfo.kcal())
                .carbs(nutrientInfo.carbs())
                .protein(nutrientInfo.protein())
                .fat(nutrientInfo.fat())
                .sugar(nutrientInfo.sugar())
                .sodium(nutrientInfo.sodium())
                .fiber(nutrientInfo.fiber())
                .saturatedFat(nutrientInfo.saturatedFat())
                .unsaturatedFat(nutrientInfo.unsaturatedFat())
                .transFat(nutrientInfo.transFat())
                .fattyAcid(nutrientInfo.fattyAcid())
                .cholesterol(nutrientInfo.cholesterol())
                .potassium(nutrientInfo.potassium())
                .alcohol(nutrientInfo.alcohol())
                .build();
    }

}
