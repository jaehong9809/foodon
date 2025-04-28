package com.foodon.foodon.food.exception;

import com.foodon.foodon.common.exception.ErrorCode;
import com.foodon.foodon.common.exception.status.BadRequestException;
import com.foodon.foodon.common.exception.status.ConflictException;
import com.foodon.foodon.meal.exception.MealErrorCode;

public class FoodException {

    public static class FoodConflictException extends ConflictException {
        public FoodConflictException(FoodErrorCode errorCode) {
            super(new ErrorCode(errorCode.getCode(), errorCode.getMessage()));
        }
    }
}
