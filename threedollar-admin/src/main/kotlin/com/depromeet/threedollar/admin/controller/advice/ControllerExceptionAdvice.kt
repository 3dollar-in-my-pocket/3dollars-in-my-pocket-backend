package com.depromeet.threedollar.admin.controller.advice

import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.common.exception.type.ErrorCode.*
import com.depromeet.threedollar.common.exception.model.ThreeDollarsBaseException
import com.depromeet.threedollar.common.utils.logger
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import org.slf4j.Logger
import org.springframework.beans.TypeMismatchException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.validation.FieldError
import org.springframework.web.HttpMediaTypeException
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingRequestValueException
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.stream.Collectors

@RestControllerAdvice
class ControllerExceptionAdvice {

    /**
     * 400 BadRequest
     * Spring Validation
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException::class)
    protected fun handleBadRequest(e: BindException): ApiResponse<Nothing> {
        val errorMessage = e.bindingResult.fieldErrors.stream()
            .map { obj: FieldError -> obj.defaultMessage }
            .collect(Collectors.joining("\n"))
        log.error("BindException: {}", errorMessage)
        return ApiResponse.error(VALIDATION_EXCEPTION, errorMessage)
    }

    /**
     * 400 BadRequest
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    protected fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ApiResponse<Nothing> {
        log.warn(e.message)
        return ApiResponse.error(VALIDATION_EXCEPTION)
    }

    /**
     * 400 BadRequest
     * RequestParam, RequestPath, RequestPart 등의 필드가 입력되지 않은 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestValueException::class)
    protected fun handle(e: MissingRequestValueException): ApiResponse<Nothing> {
        log.warn(e.message)
        return ApiResponse.error(VALIDATION_REQUEST_MISSING_EXCEPTION)
    }

    /**
     * 400 BadRequest
     * 잘못된 타입이 입력된 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TypeMismatchException::class)
    protected fun handleTypeMismatchException(e: TypeMismatchException): ApiResponse<Nothing> {
        log.warn(e.message)
        val errorCode = VALIDATION_WRONG_TYPE_EXCEPTION
        return ApiResponse.error(errorCode, "${errorCode.message} (${e.value})")
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        InvalidFormatException::class,
        ServletRequestBindingException::class
    )
    private fun handleMethodArgumentNotValidException(e: Exception): ApiResponse<Nothing> {
        log.warn(e.message)
        return ApiResponse.error(VALIDATION_EXCEPTION)
    }

    /**
     * 405 Method Not Allowed
     * 지원하지 않은 HTTP method 호출 할 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    private fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): ApiResponse<Nothing> {
        log.warn(e.message, e)
        return ApiResponse.error(METHOD_NOT_ALLOWED_EXCEPTION)
    }

    /**
     * 406 Not Acceptable
     */
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(HttpMediaTypeNotAcceptableException::class)
    protected fun handleHttpMediaTypeNotAcceptableException(e: HttpMediaTypeNotAcceptableException): ApiResponse<Nothing> {
        log.warn(e.message)
        return ApiResponse.error(NOT_ACCEPTABLE_EXCEPTION)
    }

    /**
     * 415 UnSupported Media Type
     * 지원하지 않는 미디어 타입인 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeException::class)
    private fun handleHttpMediaTypeException(e: HttpMediaTypeException): ApiResponse<Nothing> {
        log.warn(e.message, e)
        return ApiResponse.error(UNSUPPORTED_MEDIA_TYPE_EXCEPTION)
    }

    /**
     * ThreeDollars Custom Exception
     */
    @ExceptionHandler(ThreeDollarsBaseException::class)
    protected fun handleBaseException(exception: ThreeDollarsBaseException): ResponseEntity<ApiResponse<Any>> {
        log.error(exception.message, exception)
        return ResponseEntity.status(exception.status)
            .body(ApiResponse.error(exception.errorCode))
    }

    /**
     * 500 Internal Server
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    private fun handleInternalServerException(e: Exception): ApiResponse<Nothing> {
        log.error(e.message, e)
        return ApiResponse.error(INTERNAL_SERVER_EXCEPTION)
    }

    companion object {
        private val log: Logger = logger()
    }

}
