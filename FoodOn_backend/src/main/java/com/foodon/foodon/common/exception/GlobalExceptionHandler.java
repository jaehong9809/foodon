package com.foodon.foodon.common.exception;

import com.foodon.foodon.common.dto.Response;
import com.foodon.foodon.common.exception.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String LOG_FORMAT = "request = {}, {} \n class = {} \n code = {} \n message = {}";

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            GlobalException exception,
            HttpServletRequest request
    ){
        ErrorCode errorCode = exception.getErrorCode();
        log.warn(
                LOG_FORMAT,
                request.getMethod(),
                request.getRequestURI(),
                exception.getClass().getSimpleName(),
                errorCode.getCode(),
                errorCode.getMessage()
        );
        exception.printStackTrace();

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(ErrorResponse.from(errorCode));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleServerException(
            Exception exception,
            HttpServletRequest request
    ){
        String code = "500";
        String message = "서버 에러가 발생하였습니다.";
        log.warn(
                LOG_FORMAT,
                request.getMethod(),
                request.getRequestURI(),
                exception.getClass().getSimpleName(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage()
        );
        exception.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(code, message));
    }

}

