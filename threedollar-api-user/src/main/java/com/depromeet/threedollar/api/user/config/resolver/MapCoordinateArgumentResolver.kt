package com.depromeet.threedollar.api.user.config.resolver

import com.depromeet.threedollar.common.exception.type.ErrorCode.INVALID_MISSING_MAP_LATITUDE
import com.depromeet.threedollar.common.exception.type.ErrorCode.INVALID_MISSING_MAP_LONGITUDE
import com.depromeet.threedollar.common.exception.model.InternalServerException
import com.depromeet.threedollar.common.exception.model.InvalidException
import com.depromeet.threedollar.common.model.CoordinateValue
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

private const val MAP_LATITUDE = "mapLatitude"
private const val MAP_LONGITUDE = "mapLongitude"

@Component
class MapCoordinateArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(MapCoordinate::class.java)
            && CoordinateValue::class.java == parameter.parameterType
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val mapLatitude = webRequest.getParameter(MAP_LATITUDE)?.toDoubleOrNull()
        val mapLongitude = webRequest.getParameter(MAP_LONGITUDE)?.toDoubleOrNull()

        val annotation = parameter.getParameterAnnotation(MapCoordinate::class.java)
            ?: throw InternalServerException("발생할 수 없는 에러가 발생하였습니다. @MapCoordinate can't be null")

        if (annotation.required) {
            mapLatitude ?: throw InvalidException("${MAP_LATITUDE}를 입력해주세요", INVALID_MISSING_MAP_LATITUDE)
            mapLongitude ?: throw InvalidException("${MAP_LONGITUDE}를 입력해주세요", INVALID_MISSING_MAP_LONGITUDE)
        }

        return CoordinateValue.of(mapLatitude ?: 0.0, mapLongitude ?: 0.0)
    }

}
