package com.depromeet.threedollar.api.controller;

import com.depromeet.threedollar.api.common.dto.ApiResponse;
import com.depromeet.threedollar.common.exception.*;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static com.depromeet.threedollar.common.exception.ErrorCode.*;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    /**
     * 400 BadRequest
     * 잘못된 입력이 들어왔을 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ApiResponse<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(VALIDATION_EXCEPTION, Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    protected ApiResponse<Object> handleBadRequest(BindException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(VALIDATION_EXCEPTION, Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ApiResponse<Object> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(VALIDATION_EXCEPTION, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatException.class)
    protected ApiResponse<Object> handleMethodArgumentTypeMismatchException(InvalidFormatException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(VALIDATION_EXCEPTION);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    protected ApiResponse<Object> handleValidationException(final ValidationException exception) {
        log.error(exception.getMessage(), exception);
        return ApiResponse.error(exception.getErrorCode());
    }

    /**
     * 401 UnAuthorized
     * 세션에 문제가 있는 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnAuthorizedException.class)
    protected ApiResponse<Object> handleUnAuthorizedException(final UnAuthorizedException exception) {
        log.error(exception.getMessage(), exception);
        return ApiResponse.error(exception.getErrorCode());
    }

    /**
     * 403 Forbidden
     * 허용하지 않는 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    protected ApiResponse<Object> handleForbiddenException(final ForbiddenException exception) {
        log.error(exception.getMessage(), exception);
        return ApiResponse.error(exception.getErrorCode());
    }

    /**
     * 404 Not Found
     * 존재하지 않는 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    protected ApiResponse<Object> handleNotFoundException(final NotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return ApiResponse.error(exception.getErrorCode());
    }

    /**
     * 405 Method Not Allowed
     * 지원하지 않은 HTTP method 호출 할 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ApiResponse<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(METHOD_NOT_ALLOWED_EXCEPTION);
    }

    /**
     * 409 Conflict
     * 이미 존재하는 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    protected ApiResponse<Object> handleConflictException(final ConflictException exception) {
        log.error(exception.getMessage(), exception);
        return ApiResponse.error(exception.getErrorCode());
    }

    /**
     * 502 Bad Gateway
     * 외부 API 연동 중 에러가 발생할 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(BadGatewayException.class)
    protected ApiResponse<Object> handleBadGatewayException(final BadGatewayException exception) {
        log.error(exception.getMessage(), exception);
        return ApiResponse.error(exception.getErrorCode());
    }

    /**
     * 503 Service Unavailable
     * 서비스를 사용할 수 없을 때 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ServiceUnAvailableException.class)
    protected ApiResponse<Object> handleServiceUnavailableException(final ServiceUnAvailableException exception) {
        log.error(exception.getMessage(), exception);
        return ApiResponse.error(exception.getErrorCode());
    }

    /**
     * 500 Internal Server Error
     * 서버 내부에서 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    protected ApiResponse<Object> handleException(final Exception exception) {
        log.error(exception.getMessage(), exception);
        return ApiResponse.error(INTERNAL_SERVER_EXCEPTION);
    }

}
