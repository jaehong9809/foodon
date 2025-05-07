package com.foodon.foodon.meal.dto;

public record PositionInfo(
        double x,
        double y,
        double width,
        double height,
        double confidence
) {
}
