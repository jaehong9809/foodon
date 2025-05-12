package com.foodon.foodon.recommend.domain;

import com.foodon.foodon.common.entity.BaseTimeEntity;
import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "recommend_foods",
        indexes = {
                @Index(name = "idx_member_created", columnList = "member_id, created_at"),
                @Index(name = "idx_member_type_id_created", columnList = "member_id, food_type, food_id, created_at")
        }
)
public class RecommendFood extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommend_food_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodType foodType;

    @Column(nullable = false)
    private Long foodId;

    @Column(nullable = false)
    private String foodName;

    @Column(precision = 7, scale = 2, nullable = false)
    private BigDecimal kcalPerServing; // 1회 제공 열량 (100g 당 x)

}


