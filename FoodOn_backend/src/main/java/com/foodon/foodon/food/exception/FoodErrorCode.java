package com.foodon.foodon.food.exception;


import lombok.Getter;

@Getter
public enum FoodErrorCode {

    CONFLICT_CUSTOM_FOOD("50000", "이미 같은 이름으로 등록된 음식이 있습니다."),
    ;

    private final String code;
    private final String message;

    FoodErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
