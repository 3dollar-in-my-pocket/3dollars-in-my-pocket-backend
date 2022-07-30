package com.depromeet.threedollar.api.core.service.config.resolver

import com.depromeet.threedollar.common.exception.model.InternalServerException
import com.depromeet.threedollar.common.exception.model.InvalidException
import com.depromeet.threedollar.common.exception.type.ErrorCode.E400_MISSING_MAP_LATITUDE_PARAMETER
import com.depromeet.threedollar.common.exception.type.ErrorCode.E400_MISSING_MAP_LONGITUDE_PARAMETER
import com.depromeet.threedollar.common.model.LocationValue
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

private const val MAP_LATITUDE = "mapLatitude"
private const val MAP_LONGITUDE = "mapLongitude"

@Component
class MapLocationArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(MapLocation::class.java)
            && LocationValue::class.java == parameter.parameterType
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any? {
        val mapLatitude = webRequest.getParameter(MAP_LATITUDE)?.toDoubleOrNull()
        val mapLongitude = webRequest.getParameter(MAP_LONGITUDE)?.toDoubleOrNull()

        val annotation = parameter.getParameterAnnotation(MapLocation::class.java)
            ?: throw InternalServerException("예상치 못한 에러가 발생하였습니다. 컨트롤러(${parameter.declaringClass.simpleName} - ${parameter.method?.name})에 @MapLocation 어노테이션을 추가해주세요")

        if (annotation.required) {
            mapLatitude ?: throw InvalidException("${MAP_LATITUDE}를 입력해주세요", E400_MISSING_MAP_LATITUDE_PARAMETER)
            mapLongitude ?: throw InvalidException("${MAP_LONGITUDE}를 입력해주세요", E400_MISSING_MAP_LONGITUDE_PARAMETER)
        }

        return LocationValue.of(mapLatitude ?: 0.0, mapLongitude ?: 0.0)
    }

}
