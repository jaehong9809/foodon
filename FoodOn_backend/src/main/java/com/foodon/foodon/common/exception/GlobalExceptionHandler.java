package com.foodon.foodon.common.exception;

import com.foodon.foodon.common.dto.Response;
import com.foodon.foodon.common.exception.dto.ErrorResponse;
import com.foodon.foodon.common.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Response<Void>> handleGlobalException(
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

        return ResponseUtil.failure(exception, errorCode);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Void>> handleServerException(
            Exception exception,
            HttpServletRequest request
    ){
        log.warn(
                LOG_FORMAT,
                request.getMethod(),
                request.getRequestURI(),
                exception.getClass().getSimpleName(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage()
        );
        exception.printStackTrace();

        return ResponseUtil.failure(exception);
    }

}

