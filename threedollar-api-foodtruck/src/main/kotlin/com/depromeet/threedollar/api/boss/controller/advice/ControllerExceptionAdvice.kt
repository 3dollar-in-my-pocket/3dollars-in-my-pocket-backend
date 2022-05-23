package com.depromeet.threedollar.api.boss.controller.advice

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.springframework.beans.TypeMismatchException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.validation.FieldError
import org.springframework.web.HttpMediaTypeException
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingPathVariableException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.utils.HttpServletRequestUtils
import com.depromeet.threedollar.common.exception.model.ThreeDollarsBaseException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.common.exception.type.ErrorCode.INTERNAL_SERVER
import com.depromeet.threedollar.common.exception.type.ErrorCode.INVALID
import com.depromeet.threedollar.common.exception.type.ErrorCode.INVALID_MISSING_PARAMETER
import com.depromeet.threedollar.common.exception.type.ErrorCode.INVALID_TYPE
import com.depromeet.threedollar.common.exception.type.ErrorCode.INVALID_UPLOAD_FILE_SIZE
import com.depromeet.threedollar.common.exception.type.ErrorCode.METHOD_NOT_ALLOWED
import com.depromeet.threedollar.common.exception.type.ErrorCode.NOT_ACCEPTABLE
import com.depromeet.threedollar.common.exception.type.ErrorCode.UNSUPPORTED_MEDIA_TYPE
import com.depromeet.threedollar.common.model.event.ServerExceptionOccurredEvent
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.common.utils.UserMetaSessionUtils
import com.depromeet.threedollar.common.utils.logger
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException

@RestControllerAdvice
class ControllerExceptionAdvice(
    private val eventPublisher: ApplicationEventPublisher,
) {

    /**
     * 400 BadRequest
     * Spring Validation
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException::class)
    private fun handleBadRequest(e: BindException): ApiResponse<Nothing> {
        val errorMessage = e.bindingResult.fieldErrors.stream()
            .map { obj: FieldError -> obj.defaultMessage }
            .collect(Collectors.joining("\n"))
        log.error("BindException: {}", errorMessage)
        return ApiResponse.error(INVALID, errorMessage)
    }

    /**
     * 400 BadRequest
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    protected fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ApiResponse<Nothing> {
        log.warn(e.message)
        if (e.rootCause is MissingKotlinParameterException) {
            val parameterName = (e.rootCause as MissingKotlinParameterException).parameter.name
            return ApiResponse.error(INVALID_MISSING_PARAMETER, "필수 파라미터 ($parameterName)을 입력해주세요")
        }
        return ApiResponse.error(INVALID)
    }

    /**
     * 400 BadRequest
     * RequestParam 필드가 입력되지 않은 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException::class)
    protected fun handleMissingServletRequestParameterException(e: MissingServletRequestParameterException): ApiResponse<Nothing> {
        log.warn(e.message)
        return ApiResponse.error(INVALID_MISSING_PARAMETER, "필수 파라미터 (${e.parameterName})을 입력해주세요")
    }

    /**
     * 400 BadRequest
     * RequestPart 필드가 입력되지 않은 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestPartException::class)
    protected fun handleMissingServletRequestPartException(e: MissingServletRequestPartException): ApiResponse<Nothing> {
        log.warn(e.message)
        return ApiResponse.error(INVALID_MISSING_PARAMETER, "Multipart (${e.requestPartName})을 입력해주세요")
    }

    /**
     * 400 BadRequest
     * RequestPart 필수 Path Variable 가 입력되지 않은 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingPathVariableException::class)
    private fun handleMissingPathVariableException(e: MissingPathVariableException): ApiResponse<Nothing> {
        log.warn(e.message)
        return ApiResponse.error(INVALID_MISSING_PARAMETER, "Path (${e.variableName})를 입력해주세요")
    }

    /**
     * 400 BadRequest
     * 잘못된 타입이 입력된 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TypeMismatchException::class)
    private fun handleTypeMismatchException(e: TypeMismatchException): ApiResponse<Nothing> {
        log.warn(e.message)
        val errorCode = INVALID_TYPE
        return ApiResponse.error(errorCode, "${errorCode.message} (${e.value})")
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        InvalidFormatException::class,
        ServletRequestBindingException::class
    )
    private fun handleMethodArgumentNotValidException(e: Exception): ApiResponse<Nothing> {
        log.warn(e.message)
        return ApiResponse.error(INVALID)
    }

    /**
     * 405 Method Not Allowed
     * 지원하지 않은 HTTP method 호출 할 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    private fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): ApiResponse<Nothing> {
        log.warn(e.message, e)
        return ApiResponse.error(METHOD_NOT_ALLOWED)
    }

    /**
     * 406 Not Acceptable
     */
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(HttpMediaTypeNotAcceptableException::class)
    private fun handleHttpMediaTypeNotAcceptableException(e: HttpMediaTypeNotAcceptableException): ApiResponse<Nothing> {
        log.warn(e.message)
        return ApiResponse.error(NOT_ACCEPTABLE)
    }

    /**
     * 415 UnSupported Media Type
     * 지원하지 않는 미디어 타입인 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeException::class)
    private fun handleHttpMediaTypeException(e: HttpMediaTypeException): ApiResponse<Nothing> {
        log.warn(e.message, e)
        return ApiResponse.error(UNSUPPORTED_MEDIA_TYPE)
    }

    /**
     * 최대 허용한 이미지 크기를 넘은 경우 발생하는 Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException::class)
    private fun handleMaxUploadSizeExceededException(e: MaxUploadSizeExceededException): ApiResponse<Nothing> {
        log.error(e.message, e)
        return ApiResponse.error(INVALID_UPLOAD_FILE_SIZE)
    }


    /**
     * ThreeDollars Custom Exception
     */
    @ExceptionHandler(ThreeDollarsBaseException::class)
    private fun handleBaseException(exception: ThreeDollarsBaseException, request: HttpServletRequest): ResponseEntity<ApiResponse<Nothing>> {
        log.error(exception.message, exception)
        if (exception.isSetAlarm) {
            eventPublisher.publishEvent(createUnExpectedErrorOccurredEvent(exception.errorCode, exception, request))
        }
        return ResponseEntity.status(exception.status)
            .body(ApiResponse.error(exception.errorCode))
    }

    /**
     * 500 Internal Server
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    private fun handleInternalServerException(exception: Exception, request: HttpServletRequest): ApiResponse<Nothing> {
        log.error(exception.message, exception)
        eventPublisher.publishEvent(createUnExpectedErrorOccurredEvent(INTERNAL_SERVER, exception, request))
        return ApiResponse.error(INTERNAL_SERVER)
    }

    private fun createUnExpectedErrorOccurredEvent(errorCode: ErrorCode, exception: Exception, request: HttpServletRequest): ServerExceptionOccurredEvent {
        return ServerExceptionOccurredEvent.error(
            ApplicationType.BOSS_API,
            errorCode,
            exception,
            HttpServletRequestUtils.getFullUrlWithMethod(request),
            UserMetaSessionUtils.get(),
            LocalDateTime.now(ZoneId.of("Asia/Seoul"))
        )
    }

    companion object {
        private val log: Logger = logger()
    }

}
