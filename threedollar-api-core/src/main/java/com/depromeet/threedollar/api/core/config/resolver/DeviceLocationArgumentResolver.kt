package com.depromeet.threedollar.api.core.config.resolver

import com.depromeet.threedollar.common.exception.model.InternalServerException
import com.depromeet.threedollar.common.exception.model.InvalidException
import com.depromeet.threedollar.common.exception.type.ErrorCode.INVALID_MISSING_LATITUDE
import com.depromeet.threedollar.common.exception.type.ErrorCode.INVALID_MISSING_LONGITUDE
import com.depromeet.threedollar.common.model.LocationValue
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

private const val LATITUDE = "latitude"
private const val LONGITUDE = "longitude"

@Component
class DeviceLocationArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(DeviceLocation::class.java)
            && LocationValue::class.java == parameter.parameterType
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any? {
        val latitude = webRequest.getParameter(LATITUDE)?.toDoubleOrNull()
        val longitude = webRequest.getParameter(LONGITUDE)?.toDoubleOrNull()

        val annotation = parameter.getParameterAnnotation(DeviceLocation::class.java)
            ?: throw InternalServerException("예상치 못한 에러가 발생하였습니다. 컨트롤러(${parameter.declaringClass.simpleName}-${parameter.method?.name})에 @DeviceLocation 어노테이션을 추가해주세요")

        if (annotation.required) {
            latitude ?: throw InvalidException("${LATITUDE}를 입력해주세요", INVALID_MISSING_LATITUDE)
            longitude ?: throw InvalidException("${LONGITUDE}를 입력해주세요", INVALID_MISSING_LONGITUDE)
        }

        return LocationValue.of(latitude ?: 0.0, longitude ?: 0.0)
    }

}
