package com.depromeet.threedollar.api.user.config.resolver

import com.depromeet.threedollar.common.exception.type.ErrorCode.INVALID_MISSING_LATITUDE
import com.depromeet.threedollar.common.exception.type.ErrorCode.INVALID_MISSING_LONGITUDE
import com.depromeet.threedollar.common.exception.model.InternalServerException
import com.depromeet.threedollar.common.exception.model.InvalidException
import com.depromeet.threedollar.common.model.CoordinateValue
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

private const val LATITUDE = "latitude"
private const val LONGITUDE = "longitude"

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
            latitude ?: throw InvalidException("${LATITUDE}를 입력해주세요", INVALID_MISSING_LATITUDE)
            longitude ?: throw InvalidException("${LONGITUDE}를 입력해주세요", INVALID_MISSING_LONGITUDE)
        }

        return CoordinateValue.of(latitude ?: 0.0, longitude ?: 0.0)
    }

}
