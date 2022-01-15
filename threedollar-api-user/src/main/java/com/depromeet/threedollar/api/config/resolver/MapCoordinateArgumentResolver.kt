package com.depromeet.threedollar.api.config.resolver

import com.depromeet.threedollar.common.exception.type.ErrorCode.VALIDATION_MAP_LATITUDE_EXCEPTION
import com.depromeet.threedollar.common.exception.type.ErrorCode.VALIDATION_MAP_LONGITUDE_EXCEPTION
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
            mapLatitude ?: throw ValidationException("${MAP_LATITUDE}를 입력해주세요", VALIDATION_MAP_LATITUDE_EXCEPTION)
            mapLongitude ?: throw ValidationException("${MAP_LONGITUDE}를 입력해주세요", VALIDATION_MAP_LONGITUDE_EXCEPTION)
        }

        return CoordinateValue.of(mapLatitude ?: 0.0, mapLongitude ?: 0.0)
    }

    companion object {
        private const val MAP_LATITUDE = "mapLatitude"
        private const val MAP_LONGITUDE = "mapLongitude"
    }

}
