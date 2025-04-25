package com.foodon.foodon.recommend.domain;

import com.foodon.foodon.common.entity.BaseTimeEntity;
import com.foodon.foodon.food.domain.FoodType;
import com.foodon.foodon.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "recommend_foods",
        indexes = {
                @Index(name = "idx_member_food_type_food_id", columnList = "member, foodType, foodId")
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

    @Column(nullable = false)
    private FoodType foodType;

    @Column(nullable = false)
    private Long foodId;

}


