package com.foodon.foodon.meal.domain;

import com.foodon.foodon.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "meal_items")
public class MealItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    private Long publicFoodId;

    private Long customFoodId;

    @Column(precision = 2, scale = 1)
    private BigDecimal quantity;

    private BoundingBox boundingBox; // 사진상에서의 위치

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean isDeleted = false;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean isRecommended = false;


}
