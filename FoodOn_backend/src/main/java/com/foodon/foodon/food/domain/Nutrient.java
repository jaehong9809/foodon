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

    @Column(precision = 7, scale = 2, nullable = false)
    private BigDecimal kcal;

    @Column(precision = 5, scale = 2)
    private BigDecimal carbs; // 탄수화물

    @Column(precision = 5, scale = 2)
    private BigDecimal protein; // 단백질

    @Column(precision = 5, scale = 2)
    private BigDecimal fat; // 지방

    @Column(precision = 5, scale = 2)
    private BigDecimal sugar; // 당류

    @Column(precision = 7, scale = 2)
    private BigDecimal sodium; // 나트륨

    @Column(precision = 6, scale = 2)
    private BigDecimal fiber; // 식이섬유

    @Column(precision = 5, scale = 2)
    private BigDecimal saturatedFat; // 포화지방산

    @Column(precision = 5, scale = 2)
    private BigDecimal unsaturatedFat; // 불포화지방산

    @Column(precision = 5, scale = 2)
    private BigDecimal transFat; // 트랜스지방산

    @Column(precision = 5, scale = 2)
    private BigDecimal fattyAcid; // 지방산

    @Column(precision = 5, scale = 2)
    private BigDecimal cholesterol; // 콜레스테롤

    @Column(precision = 7, scale = 2)
    private BigDecimal potassium; // 칼륨

    @Column(precision = 5, scale = 2)
    private BigDecimal caffeine;

    @Column(precision = 5, scale = 2)
    private BigDecimal alcohol; // 알코올

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
