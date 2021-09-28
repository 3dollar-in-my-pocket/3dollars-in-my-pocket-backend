package com.depromeet.threedollar.admin.controller.advice

import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.common.exception.ErrorCode.*
import com.depromeet.threedollar.common.exception.model.*
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.HttpMediaTypeException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestValueException
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerExceptionAdvice {

    private val log = LoggerFactory.getLogger(ControllerExceptionAdvice::class.java)

    /**
     * 400 BAD Request
     * 잘못된 입력이 들어왔을 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException::class)
    private fun handleBadRequest(e: BindException): ApiResponse<Nothing> {
        log.error(e.message, e)
        return ApiResponse.error(VALIDATION_EXCEPTION, e.bindingResult.allErrors[0].defaultMessage)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        MethodArgumentNotValidException::class,
        InvalidFormatException::class,
        MissingRequestValueException::class,
        ServletRequestBindingException::class
    )
    private fun handleMethodArgumentNotValidException(e: Exception): ApiResponse<Nothing> {
        log.error(e.message, e)
        return ApiResponse.error(VALIDATION_EXCEPTION)
    }

    /**
     * 405 Method Not Allowed
     * 지원하지 않은 HTTP method 호출 할 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    private fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): ApiResponse<Nothing> {
        log.error(e.message, e)
        return ApiResponse.error(METHOD_NOT_ALLOWED_EXCEPTION)
    }

    /**
     * 415 UnSupported Media Type
     * 지원하지 않는 미디어 타입인 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeException::class)
    private fun handleHttpMediaTypeException(e: HttpMediaTypeException): ApiResponse<Nothing> {
        log.error(e.message, e)
        return ApiResponse.error(UNSUPPORTED_MEDIA_TYPE)
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

}
