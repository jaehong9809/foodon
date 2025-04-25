package com.foodon.foodon.meal.exception;

import com.foodon.foodon.common.exception.ErrorCode;
import com.foodon.foodon.common.exception.status.BadRequestException;
import com.foodon.foodon.image.exception.ImageErrorCode;

public class MealException {

    public static class MealBadRequestException extends BadRequestException {
        public MealBadRequestException(MealErrorCode errorCode) {
            super(new ErrorCode(errorCode.getCode(), errorCode.getMessage()));
        }
    }
}
