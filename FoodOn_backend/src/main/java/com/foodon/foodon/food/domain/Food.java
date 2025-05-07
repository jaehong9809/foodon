package com.foodon.foodon.food.domain;

import com.foodon.foodon.food.dto.CustomFoodCreateRequest;
import com.foodon.foodon.meal.domain.MealItem;
import com.foodon.foodon.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "foods",
        indexes = {
            @Index(name = "idx_food_name", columnList = "food_name"),
            @Index(name = "idx_food_member_name", columnList = "member_id, food_name")
        }
)
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Long id;

    private Long memberId; // 커스텀

    @Column(name = "food_name", unique = true, nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String category;

    @Column(precision = 7, scale = 2)
    private BigDecimal servingSize;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Unit unit;


    private Food(
            Long memberId,
            String name,
            BigDecimal servingSize,
            Unit unit
    ){
        this.memberId = memberId;
        this.name = name;
        this.servingSize = servingSize;
        this.unit = unit;
    }

    public static Food createCustomFoodByMember(
        CustomFoodCreateRequest request,
        Member member
    ) {
        return new Food(
                member.getId(),
                request.foodName(),
                request.servingSize(),
                request.unit()
        );
    }

}
