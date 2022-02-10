package com.depromeet.threedollar.api.controller.advice;

import com.depromeet.threedollar.common.type.ApplicationType;
import com.depromeet.threedollar.common.utils.UserMetaSessionUtils;
import com.depromeet.threedollar.domain.common.event.ServerExceptionOccurredEvent;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.common.exception.type.ErrorCode;
import com.depromeet.threedollar.common.exception.model.ThreeDollarsBaseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.*;
import static java.util.stream.Collectors.joining;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ControllerExceptionAdvice {

    private final ApplicationEventPublisher eventPublisher;

    /**
     * 400 BadRequest
     * Spring Validation
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    protected ApiResponse<Object> handleBadRequest(BindException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(joining("\n"));
        log.error("BindException: {}", errorMessage);
        return ApiResponse.error(INVALID, errorMessage);
    }

    /**
     * 400 BadRequest
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ApiResponse<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn(e.getMessage());
        return ApiResponse.error(INVALID);
    }

    /**
     * 400 BadRequest
     * RequestParam 필수 필드가 입력되지 않은 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ApiResponse<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.warn(e.getMessage());
        return ApiResponse.error(INVALID_MISSING_PARAMETER, String.format("Parameter (%s)를 입력해주세요", e.getParameterName()));
    }

    /**
     * 400 BadRequest
     * RequestPart 필수 필드가 입력되지 않은 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestPartException.class)
    protected ApiResponse<Object> handleMissingServletRequestParameterException(MissingServletRequestPartException e) {
        log.warn(e.getMessage());
        return ApiResponse.error(INVALID_MISSING_PARAMETER, String.format("Multipart (%s)를 입력해주세요", e.getRequestPartName()));
    }

    /**
     * 400 BadRequest
     * RequestPart 필수 Path Variable 가 입력되지 않은 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingPathVariableException.class)
    protected ApiResponse<Object> handleMissingPathVariableException(MissingPathVariableException e) {
        log.warn(e.getMessage());
        return ApiResponse.error(INVALID_MISSING_PARAMETER, String.format("Path (%s)를 입력해주세요", e.getVariableName()));
    }

    /**
     * 400 BadRequest
     * 잘못된 타입이 입력된 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TypeMismatchException.class)
    protected ApiResponse<Object> handleTypeMismatchException(TypeMismatchException e) {
        log.warn(e.getMessage());
        return ApiResponse.error(INVALID_TYPE, String.format("%s (%s)", INVALID_TYPE.getMessage(), e.getValue()));
    }

    /**
     * 400 BadRequest
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
        InvalidFormatException.class,
        ServletRequestBindingException.class
    })
    protected ApiResponse<Object> handleInvalidFormatException(Exception e) {
        log.warn(e.getMessage());
        return ApiResponse.error(INVALID);
    }

    /**
     * 405 Method Not Allowed
     * 지원하지 않은 HTTP method 호출 할 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ApiResponse<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn(e.getMessage());
        return ApiResponse.error(METHOD_NOT_ALLOWED);
    }

    /**
     * 406 Not Acceptable
     */
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    protected ApiResponse<Object> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException e) {
        log.warn(e.getMessage());
        return ApiResponse.error(NOT_ACCEPTABLE);
    }

    /**
     * 415 UnSupported Media Type
     * 지원하지 않는 미디어 타입인 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeException.class)
    protected ApiResponse<Object> handleHttpMediaTypeException(HttpMediaTypeException e) {
        log.warn(e.getMessage(), e);
        return ApiResponse.error(UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * 최대 허용한 이미지 크기를 넘은 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected ApiResponse<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error(e.getMessage(), e);
        eventPublisher.publishEvent(createUnExpectedErrorOccurredEvent(INVALID_UPLOAD_FILE_SIZE, e));
        return ApiResponse.error(INVALID_UPLOAD_FILE_SIZE);
    }

    /**
     * ThreeDollars Custom Exception
     */
    @ExceptionHandler(ThreeDollarsBaseException.class)
    protected ResponseEntity<ApiResponse<Object>> handleBaseException(ThreeDollarsBaseException exception) {
        if (exception.isSetAlarm()) {
            eventPublisher.publishEvent(createUnExpectedErrorOccurredEvent(exception.getErrorCode(), exception));
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
    protected ApiResponse<Object> handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        eventPublisher.publishEvent(createUnExpectedErrorOccurredEvent(INTERNAL_SERVER, exception));
        return ApiResponse.error(INTERNAL_SERVER);
    }

    private ServerExceptionOccurredEvent createUnExpectedErrorOccurredEvent(ErrorCode errorCode, Exception exception) {
        return ServerExceptionOccurredEvent.error(
            ApplicationType.USER_API,
            errorCode,
            exception,
            UserMetaSessionUtils.get(),
            LocalDateTime.now(ZoneId.of("Asia/Seoul"))
        );
    }

}
