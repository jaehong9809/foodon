package com.foodon.foodon.meal.domain;

import com.foodon.foodon.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "meals")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private MealTimeType mealTimeType;

    @Column(nullable = false)
    private LocalDateTime mealTime;

    @Column(nullable = false)
    private int totalKcal;

    @Column(nullable = false)
    private int totalCarbs;

    @Column(nullable = false)
    private int totalProtein;

    @Column(nullable = false)
    private int totalFat;

    private String mealImage;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean isDeleted = false;

}
