package com.depromeet.threedollar.api.config.resolver

import com.depromeet.threedollar.common.exception.ErrorCode.VALIDATION_MAP_LATITUDE_EXCEPTION
import com.depromeet.threedollar.common.exception.ErrorCode.VALIDATION_MAP_LONGITUDE_EXCEPTION
import com.depromeet.threedollar.common.exception.model.InternalServerException
import com.depromeet.threedollar.common.exception.model.ValidationException
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class MapCoordinateArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.getParameterAnnotation(MapCoordinate::class.java) != null
            && Coordinate::class.java == parameter.parameterType
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val mapLatitude = webRequest.getParameter("mapLatitude")?.toDoubleOrNull()
        val mapLongitude = webRequest.getParameter("mapLongitude")?.toDoubleOrNull()

        val annotation = parameter.getParameterAnnotation(MapCoordinate::class.java)
            ?: throw InternalServerException("예상치 못한 에러가 발생하였습니다. @MapCoordinate이 null일 수 없습니다")

        if (annotation.required) {
            mapLatitude ?: throw ValidationException("mapLatitude를 입력해주세요", VALIDATION_MAP_LATITUDE_EXCEPTION)
            mapLongitude ?: throw ValidationException("mapLongitude를 입력해주세요", VALIDATION_MAP_LONGITUDE_EXCEPTION)
        }

        return Coordinate.of(mapLatitude, mapLongitude)
    }

}
