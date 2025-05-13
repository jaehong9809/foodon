package com.foodon.foodon.meal.domain;

import com.foodon.foodon.meal.dto.PositionInfo;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Position {
    private double x;
    private double y;
    private double width;
    private double height;
    private double confidence;

    private Position(
            double x,
            double y,
            double width,
            double height,
            double confidence
    ) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.confidence = confidence;
    }

    public static Position of(PositionInfo positionInfo) {
        return new Position(
                positionInfo.x(),
                positionInfo.y(),
                positionInfo.width(),
                positionInfo.height(),
                positionInfo.confidence()
        );
    }

}
