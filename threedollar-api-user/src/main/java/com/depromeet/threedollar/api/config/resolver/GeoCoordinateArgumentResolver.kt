package com.depromeet.threedollar.api.config.resolver

import com.depromeet.threedollar.common.exception.type.ErrorCode.VALIDATION_LATITUDE_EXCEPTION
import com.depromeet.threedollar.common.exception.type.ErrorCode.VALIDATION_LONGITUDE_EXCEPTION
import com.depromeet.threedollar.common.exception.model.InternalServerException
import com.depromeet.threedollar.common.exception.model.ValidationException
import com.depromeet.threedollar.common.model.CoordinateValue
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class GeoCoordinateArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(GeoCoordinate::class.java)
            && CoordinateValue::class.java == parameter.parameterType
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val latitude = webRequest.getParameter(LATITUDE)?.toDoubleOrNull()
        val longitude = webRequest.getParameter(LONGITUDE)?.toDoubleOrNull()

        val annotation = parameter.getParameterAnnotation(GeoCoordinate::class.java)
            ?: throw InternalServerException("발생할 수 없는 에러가 발생하였습니다. @GeoCoordinate can't be null")

        if (annotation.required) {
            latitude ?: throw ValidationException("${LATITUDE}를 입력해주세요", VALIDATION_LATITUDE_EXCEPTION)
            longitude ?: throw ValidationException("${LONGITUDE}를 입력해주세요", VALIDATION_LONGITUDE_EXCEPTION)
        }

        return CoordinateValue.of(latitude ?: 0.0, longitude ?: 0.0)
    }

    companion object {
        private const val LATITUDE = "latitude"
        private const val LONGITUDE = "longitude"
    }

}
