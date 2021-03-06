package com.depromeet.threedollar.api.bossservice.config.advice

import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse
import com.depromeet.threedollar.api.core.common.utils.HttpServletRequestUtils
import com.depromeet.threedollar.common.exception.model.ThreeDollarsBaseException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.common.model.event.ServerExceptionOccurredEvent
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.common.utils.UserMetaSessionUtils
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import mu.KotlinLogging
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
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class ControllerExceptionAdvice(
    private val eventPublisher: ApplicationEventPublisher,
) {

    private val log = KotlinLogging.logger {}

    /**
     * 400 BadRequest
     * Spring Validation
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException::class)
    private fun handleBadRequest(e: BindException): ApiResponse<Nothing> {
        val errorMessage = e.bindingResult.fieldErrors.stream()
            .map { fieldError: FieldError -> fieldError.defaultMessage }
            .collect(Collectors.joining("\n"))
        log.error("BindException: {}", errorMessage)
        return ApiResponse.error(ErrorCode.E400_INVALID, errorMessage)
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
            return ApiResponse.error(ErrorCode.E400_MISSING_PARAMETER, "?????? ???????????? ($parameterName)??? ??????????????????")
        }
        return ApiResponse.error(ErrorCode.E400_INVALID)
    }

    /**
     * 400 BadRequest
     * RequestParam ????????? ???????????? ?????? ?????? ???????????? Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException::class)
    protected fun handleMissingServletRequestParameterException(e: MissingServletRequestParameterException): ApiResponse<Nothing> {
        log.warn(e.message)
        return ApiResponse.error(ErrorCode.E400_MISSING_PARAMETER, "?????? ???????????? (${e.parameterName})??? ??????????????????")
    }

    /**
     * 400 BadRequest
     * RequestPart ????????? ???????????? ?????? ?????? ???????????? Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestPartException::class)
    protected fun handleMissingServletRequestPartException(e: MissingServletRequestPartException): ApiResponse<Nothing> {
        log.warn(e.message)
        return ApiResponse.error(ErrorCode.E400_MISSING_PARAMETER, "Multipart (${e.requestPartName})??? ??????????????????")
    }

    /**
     * 400 BadRequest
     * RequestPart ?????? Path Variable ??? ???????????? ?????? ?????? ???????????? Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingPathVariableException::class)
    private fun handleMissingPathVariableException(e: MissingPathVariableException): ApiResponse<Nothing> {
        log.warn(e.message)
        return ApiResponse.error(ErrorCode.E400_MISSING_PARAMETER, "Path (${e.variableName})??? ??????????????????")
    }

    /**
     * 400 BadRequest
     * ????????? ????????? ????????? ?????? ???????????? Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TypeMismatchException::class)
    private fun handleTypeMismatchException(e: TypeMismatchException): ApiResponse<Nothing> {
        log.warn(e.message)
        val errorCode = ErrorCode.E400_INVALID
        return ApiResponse.error(errorCode, "${errorCode.message} (${e.value})")
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        InvalidFormatException::class,
        ServletRequestBindingException::class
    )
    private fun handleMethodArgumentNotValidException(e: Exception): ApiResponse<Nothing> {
        log.warn(e.message)
        return ApiResponse.error(ErrorCode.E400_INVALID)
    }

    /**
     * 405 Method Not Allowed
     * ???????????? ?????? HTTP method ?????? ??? ?????? ???????????? Exception
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    private fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): ApiResponse<Nothing> {
        log.warn(e.message, e)
        return ApiResponse.error(ErrorCode.E405_METHOD_NOT_ALLOWED)
    }

    /**
     * 406 Not Acceptable
     */
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(HttpMediaTypeNotAcceptableException::class)
    private fun handleHttpMediaTypeNotAcceptableException(e: HttpMediaTypeNotAcceptableException): ApiResponse<Nothing> {
        log.warn(e.message)
        return ApiResponse.error(ErrorCode.E406_NOT_ACCEPTABLE)
    }

    /**
     * 415 UnSupported Media Type
     * ???????????? ?????? ????????? ????????? ?????? ???????????? Exception
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeException::class)
    private fun handleHttpMediaTypeException(e: HttpMediaTypeException): ApiResponse<Nothing> {
        log.warn(e.message, e)
        return ApiResponse.error(ErrorCode.E415_UNSUPPORTED_MEDIA_TYPE)
    }

    /**
     * ?????? ????????? ????????? ????????? ?????? ?????? ???????????? Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException::class)
    private fun handleMaxUploadSizeExceededException(e: MaxUploadSizeExceededException): ApiResponse<Nothing> {
        log.error(e.message, e)
        return ApiResponse.error(ErrorCode.E400_INVALID_FILE_SIZE_TOO_LARGE)
    }

    /**
     * CircuitBreakerException
     */
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(CallNotPermittedException::class)
    private fun handleCircuitBreakerException(exception: CallNotPermittedException, request: HttpServletRequest): ApiResponse<Nothing> {
        log.error(exception.message, exception)
        eventPublisher.publishEvent(createUnExpectedErrorOccurredEvent(ErrorCode.E502_BAD_GATEWAY, exception, request))
        return ApiResponse.error(ErrorCode.E502_BAD_GATEWAY)
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
        eventPublisher.publishEvent(createUnExpectedErrorOccurredEvent(ErrorCode.E500_INTERNAL_SERVER, exception, request))
        return ApiResponse.error(ErrorCode.E500_INTERNAL_SERVER)
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

}
