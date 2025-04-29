package com.foodon.foodon.activitylevel.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "activity_levels")
public class ActivityLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_level_id")
    private Long id;

    @Column(nullable = false, name = "activity_level_description", length = 50)
    private String description;

    @Column(nullable = false, precision = 4, scale = 3)
    private BigDecimal value;

}
