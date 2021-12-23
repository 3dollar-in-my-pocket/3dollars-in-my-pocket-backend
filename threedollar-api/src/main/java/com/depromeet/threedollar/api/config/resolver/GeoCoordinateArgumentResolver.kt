package com.depromeet.threedollar.api.config.resolver

import com.depromeet.threedollar.common.exception.ErrorCode.VALIDATION_LATITUDE_EXCEPTION
import com.depromeet.threedollar.common.exception.ErrorCode.VALIDATION_LONGITUDE_EXCEPTION
import com.depromeet.threedollar.common.exception.model.InternalServerException
import com.depromeet.threedollar.common.exception.model.ValidationException
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class GeoCoordinateArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.getParameterAnnotation(GeoCoordinate::class.java) != null
            && Coordinate::class.java == parameter.parameterType
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val latitude = webRequest.getParameter("latitude")?.toDoubleOrNull()
        val longitude = webRequest.getParameter("longitude")?.toDoubleOrNull()

        val annotation = parameter.getParameterAnnotation(GeoCoordinate::class.java)
            ?: throw InternalServerException("예상치 못한 에러가 발생하였습니다. @GeoCoordinate can't be null")

        if (annotation.required) {
            latitude ?: throw ValidationException("latitude를 입력해주세요", VALIDATION_LATITUDE_EXCEPTION)
            longitude ?: throw ValidationException("longitude를 입력해주세요", VALIDATION_LONGITUDE_EXCEPTION)
        }

        return Coordinate.of(latitude ?: 0.0, longitude ?: 0.0)
    }

}
