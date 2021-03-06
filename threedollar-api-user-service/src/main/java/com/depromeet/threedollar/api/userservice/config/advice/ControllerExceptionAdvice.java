package com.depromeet.threedollar.api.userservice.config.advice;

import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse;
import com.depromeet.threedollar.api.core.common.utils.HttpServletRequestUtils;
import com.depromeet.threedollar.common.exception.model.ThreeDollarsBaseException;
import com.depromeet.threedollar.common.exception.type.ErrorCode;
import com.depromeet.threedollar.common.model.event.ServerExceptionOccurredEvent;
import com.depromeet.threedollar.common.type.ApplicationType;
import com.depromeet.threedollar.common.utils.UserMetaSessionUtils;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
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

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.E400_INVALID;
import static com.depromeet.threedollar.common.exception.type.ErrorCode.E400_INVALID_FILE_SIZE_TOO_LARGE;
import static com.depromeet.threedollar.common.exception.type.ErrorCode.E400_MISSING_PARAMETER;
import static com.depromeet.threedollar.common.exception.type.ErrorCode.E405_METHOD_NOT_ALLOWED;
import static com.depromeet.threedollar.common.exception.type.ErrorCode.E406_NOT_ACCEPTABLE;
import static com.depromeet.threedollar.common.exception.type.ErrorCode.E415_UNSUPPORTED_MEDIA_TYPE;
import static com.depromeet.threedollar.common.exception.type.ErrorCode.E500_INTERNAL_SERVER;
import static com.depromeet.threedollar.common.exception.type.ErrorCode.E502_BAD_GATEWAY;
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
    private ApiResponse<Object> handleBadRequest(BindException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(joining("\n"));
        log.error("BindException: {}", errorMessage);
        return ApiResponse.error(E400_INVALID, errorMessage);
    }

    /**
     * 400 BadRequest
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ApiResponse<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn(e.getMessage());
        if (e.getRootCause() instanceof MissingKotlinParameterException) {
            Throwable throwable = e.getRootCause();
            if (throwable == null) {
                return ApiResponse.error(E400_INVALID);
            }
            String parameterName = ((MissingKotlinParameterException) throwable).getParameter().getName();
            return ApiResponse.error(E400_INVALID, String.format("?????? ???????????? (%s)??? ??????????????????", parameterName));
        }
        return ApiResponse.error(E400_INVALID);
    }

    /**
     * 400 BadRequest
     * RequestParam ?????? ????????? ???????????? ?????? ?????? ???????????? Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    private ApiResponse<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.warn(e.getMessage());
        return ApiResponse.error(E400_MISSING_PARAMETER, String.format("?????? ???????????? (%s)??? ??????????????????", e.getParameterName()));
    }

    /**
     * 400 BadRequest
     * RequestPart ?????? ????????? ???????????? ?????? ?????? ???????????? Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestPartException.class)
    private ApiResponse<Object> handleMissingServletRequestParameterException(MissingServletRequestPartException e) {
        log.warn(e.getMessage());
        return ApiResponse.error(E400_MISSING_PARAMETER, String.format("Multipart (%s)??? ??????????????????", e.getRequestPartName()));
    }

    /**
     * 400 BadRequest
     * RequestPart ?????? Path Variable ??? ???????????? ?????? ?????? ???????????? Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingPathVariableException.class)
    private ApiResponse<Object> handleMissingPathVariableException(MissingPathVariableException e) {
        log.warn(e.getMessage());
        return ApiResponse.error(E400_MISSING_PARAMETER, String.format("Path (%s)??? ??????????????????", e.getVariableName()));
    }

    /**
     * 400 BadRequest
     * ????????? ????????? ????????? ?????? ???????????? Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TypeMismatchException.class)
    private ApiResponse<Object> handleTypeMismatchException(TypeMismatchException e) {
        log.warn(e.getMessage());
        return ApiResponse.error(E400_INVALID, String.format("%s (%s)", E400_INVALID.getMessage(), e.getValue()));
    }

    /**
     * 400 BadRequest
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
        InvalidFormatException.class,
        ServletRequestBindingException.class
    })
    private ApiResponse<Object> handleInvalidFormatException(Exception e) {
        log.warn(e.getMessage());
        return ApiResponse.error(E400_INVALID);
    }

    /**
     * 405 Method Not Allowed
     * ???????????? ?????? HTTP method ?????? ??? ?????? ???????????? Exception
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private ApiResponse<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn(e.getMessage());
        return ApiResponse.error(E405_METHOD_NOT_ALLOWED);
    }

    /**
     * 406 Not Acceptable
     */
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    private ApiResponse<Object> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException e) {
        log.warn(e.getMessage());
        return ApiResponse.error(E406_NOT_ACCEPTABLE);
    }

    /**
     * 415 UnSupported Media Type
     * ???????????? ?????? ????????? ????????? ?????? ???????????? Exception
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeException.class)
    private ApiResponse<Object> handleHttpMediaTypeException(HttpMediaTypeException e) {
        log.warn(e.getMessage(), e);
        return ApiResponse.error(E415_UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * ?????? ????????? ????????? ????????? ?????? ?????? ???????????? Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    private ApiResponse<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(E400_INVALID_FILE_SIZE_TOO_LARGE);
    }

    /**
     * CircuitBreakerException
     */
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(CallNotPermittedException.class)
    private ApiResponse<Object> handleCircuitBreakerException(CallNotPermittedException exception, HttpServletRequest request) {
        log.error(exception.getMessage(), exception);
        eventPublisher.publishEvent(createUnExpectedErrorOccurredEvent(E502_BAD_GATEWAY, exception, request));
        return ApiResponse.error(E502_BAD_GATEWAY);
    }

    /**
     * ThreeDollars Custom Exception
     */
    @ExceptionHandler(ThreeDollarsBaseException.class)
    private ResponseEntity<ApiResponse<Object>> handleBaseException(ThreeDollarsBaseException exception, HttpServletRequest request) {
        if (exception.isSetAlarm()) {
            eventPublisher.publishEvent(createUnExpectedErrorOccurredEvent(exception.getErrorCode(), exception, request));
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
    private ApiResponse<Object> handleException(Exception exception, HttpServletRequest request) {
        log.error(exception.getMessage(), exception);
        eventPublisher.publishEvent(createUnExpectedErrorOccurredEvent(E500_INTERNAL_SERVER, exception, request));
        return ApiResponse.error(E500_INTERNAL_SERVER);
    }

    private ServerExceptionOccurredEvent createUnExpectedErrorOccurredEvent(ErrorCode errorCode, Exception exception, HttpServletRequest request) {
        return ServerExceptionOccurredEvent.error(
            ApplicationType.USER_API,
            errorCode,
            exception,
            HttpServletRequestUtils.getFullUrlWithMethod(request),
            UserMetaSessionUtils.get(),
            LocalDateTime.now(ZoneId.of("Asia/Seoul"))
        );
    }

}
