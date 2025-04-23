package com.foodon.foodon.meal.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BoundingBox {
    private double x;
    private double y;
    private double width;
    private double height;
}
