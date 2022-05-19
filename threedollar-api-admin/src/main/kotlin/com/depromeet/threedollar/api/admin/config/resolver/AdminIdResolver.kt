package com.depromeet.threedollar.api.admin.config.resolver

import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import com.depromeet.threedollar.api.admin.config.session.SessionConstants
import com.depromeet.threedollar.common.exception.model.InternalServerException

@Configuration
class AccountIdResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(AdminId::class.java) && parameter.parameterType == Long::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any {
        return webRequest.getAttribute(SessionConstants.ADMIN_ID, 0)
            ?: throw InternalServerException("예상치 못한 에러가 발생하였습니다. 컨트롤러(${parameter.declaringClass.simpleName} - ${parameter.method?.name})에서 adminId를 받아오지 못했습니다")
    }

}
