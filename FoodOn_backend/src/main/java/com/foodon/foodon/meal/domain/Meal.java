package com.foodon.foodon.meal.domain;

import com.foodon.foodon.meal.dto.MealCreateRequest;
import com.foodon.foodon.meal.dto.MealItemInfo;
import com.foodon.foodon.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalTime.parse;

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

    @OneToMany(mappedBy = "meal", cascade = CascadeType.PERSIST)
    private List<MealItem> mealItems = new ArrayList<>();

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean isDeleted = false;

    private Meal(
            Member member,
            MealTimeType mealTimeType,
            int totalKcal,
            int totalCarbs,
            int totalProtein,
            int totalFat,
            LocalDateTime mealTime,
            String mealImage
    ) {

        this.member = member;
        this.mealTimeType = mealTimeType;
        this.totalKcal = totalKcal;
        this.totalCarbs = totalCarbs;
        this.totalProtein = totalProtein;
        this.totalFat = totalFat;
        this.mealTime = mealTime;
        this.mealImage = mealImage;
    }

    public static Meal createMeal(
            Member member,
            MealCreateRequest request
    ) {

        return new Meal(
                member,
                request.mealTimeType(),
                request.totalKcal(),
                request.totalCarbs(),
                request.totalProtein(),
                request.totalFat(),
                LocalDate.now().atTime(parse(request.mealTime())),
                request.imageUrl()
        );
    }

    public void addMealItem(MealItem mealItem) {
        this.mealItems.add(mealItem);
    }
}
