package com.foodon.foodon.auth.exception;

import lombok.Getter;

@Getter
public class InvalidJwtException extends RuntimeException {
    private final int code;
    private final String message;

    public InvalidJwtException(AuthErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}