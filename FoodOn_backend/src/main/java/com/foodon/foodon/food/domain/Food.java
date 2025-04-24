package com.foodon.foodon.food.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static com.foodon.foodon.food.domain.FoodType.PUBLIC;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "foods")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Long id;

    @Column(name = "food_name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodType foodType = PUBLIC;

    @Column(precision = 5, scale = 2)
    private BigDecimal servingSize;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Unit unit;

    @Embedded
    private Nutrient nutrient;

}
