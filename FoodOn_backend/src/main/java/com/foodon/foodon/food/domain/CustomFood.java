package com.foodon.foodon.food.domain;

import com.foodon.foodon.common.entity.BaseTimeEntity;
import com.foodon.foodon.food.dto.CustomFoodCreateRequest;
import com.foodon.foodon.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static com.foodon.foodon.food.domain.FoodType.CUSTOM;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "custom_foods", indexes = {
        @Index(name = "idx_custom_food_member_food_name", columnList = "member_id, food_name")
})
public class CustomFood extends BaseTimeEntity implements FoodInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "custom_food_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "food_name", nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodType foodType = CUSTOM;

    @Column(precision = 5, scale = 2)
    private BigDecimal servingSize;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    @Embedded
    private Nutrient nutrient;

    private CustomFood(
            Member member,
            String name,
            BigDecimal servingSize,
            Unit unit,
            Nutrient nutrient
    ) {
        this.member = member;
        this.name = name;
        this.servingSize = servingSize;
        this.unit = unit;
        this.nutrient = nutrient;
    }

    public static CustomFood createCustomFoodOfMember(
            CustomFoodCreateRequest request,
            Member member
    ) {
        return new CustomFood(
                member,
                request.foodName(),
                request.servingSize(),
                request.unit(),
                Nutrient.of(request.nutrientInfo())
        );
    }

}
