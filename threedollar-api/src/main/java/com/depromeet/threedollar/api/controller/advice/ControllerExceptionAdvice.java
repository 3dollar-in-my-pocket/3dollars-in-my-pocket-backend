package com.depromeet.threedollar.api.controller.advice;

import com.depromeet.threedollar.api.event.ServerErrorOccuredEvent;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.common.exception.*;
import com.depromeet.threedollar.common.exception.model.ThreeDollarsBaseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import static com.depromeet.threedollar.common.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ControllerExceptionAdvice {

    private final ApplicationEventPublisher eventPublisher;

    /**
     * 400 BadRequest
     * 잘못된 입력이 들어왔을 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    protected ApiResponse<Object> handleBadRequest(final BindException e) {
        log.error(e.getMessage());
        return ApiResponse.error(VALIDATION_EXCEPTION, Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
        HttpMessageNotReadableException.class,
        InvalidFormatException.class,
        MissingRequestValueException.class,
        ServletRequestBindingException.class
    })
    protected ApiResponse<Object> handleInvalidFormatException(final Exception e) {
        log.error(e.getMessage());
        return ApiResponse.error(VALIDATION_EXCEPTION);
    }

    /**
     * 405 Method Not Allowed
     * 지원하지 않은 HTTP method 호출 할 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ApiResponse<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage());
        return ApiResponse.error(METHOD_NOT_ALLOWED_EXCEPTION);
    }

    /**
     * 415 UnSupported Media Type
     * 지원하지 않는 미디어 타입인 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeException.class)
    protected ApiResponse<Object> handleHttpMediaTypeException(final HttpMediaTypeException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(UNSUPPORTED_MEDIA_TYPE_EXCEPTION);
    }

    /**
     * ThreeDollars Custom Exception
     */
    @ExceptionHandler(ThreeDollarsBaseException.class)
    protected ResponseEntity<ApiResponse<Object>> handleBaseException(ThreeDollarsBaseException exception) {
        if (exception.isSetAlarm()) {
            eventPublisher.publishEvent(createEvent(exception.getErrorCode(), exception));
        }
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(exception.getStatus())
            .body(ApiResponse.error(exception.getErrorCode()));
    }

    /**
     * 500 Internal Server
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    protected ApiResponse<Object> handleException(final Exception exception) {
        log.error(exception.getMessage(), exception);
        eventPublisher.publishEvent(createEvent(INTERNAL_SERVER_EXCEPTION, exception));
        return ApiResponse.error(INTERNAL_SERVER_EXCEPTION);
    }

    private ServerErrorOccuredEvent createEvent(ErrorCode errorCode, Exception exception) {
        return ServerErrorOccuredEvent.error(errorCode, exception, LocalDateTime.now(ZoneId.of("Asia/Seoul")));
    }

}
