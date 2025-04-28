package com.foodon.foodon.meal.exception;

import lombok.Getter;

@Getter
public enum MealErrorCode {

    MEAL_ITEM_IS_NULL("40000", "식단에 기록할 음식 정보가 없습니다.")
    ;

    private final String code;
    private final String message;

    MealErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
