package com.foodon.foodon.auth.exception;

import lombok.Getter;

@Getter
public enum AuthErrorCode {

    UNAUTHORIZED_ACCESS(3000, "접근할 수 없는 리소스입니다."),
    INVALID_REFRESH_TOKEN(3001, "유효하지 않은 Refresh Token입니다."),
    FAILED_TO_VALIDATE_TOKEN(3002, "토큰 검증에 실패했습니다."),
    INVALID_ACCESS_TOKEN(3003, "유효하지 않은 Access Token입니다.");

    private final int code;
    private final String message;

    AuthErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}