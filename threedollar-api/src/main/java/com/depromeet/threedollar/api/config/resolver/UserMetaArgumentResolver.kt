package com.depromeet.threedollar.api.config.resolver

import com.depromeet.threedollar.common.type.PlatformType
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class UserMetaArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(UserMeta::class.java) && UserMetaInfo::class.java == parameter.parameterType
    }

    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any? {
        val userAgent = webRequest.getHeader(USER_AGENT_HEADER).toString()
        val sourceIp = webRequest.getHeader(SOURCE_IP_HEADER).toString()
        return UserMetaInfo(
            platformType = PlatformType.findByUserAgent(userAgent),
            userAgent = userAgent,
            sourceIp = sourceIp
        )
    }

    companion object {
        private const val USER_AGENT_HEADER = "User-Agent"
        private const val SOURCE_IP_HEADER = "X-Forwarded-For"
    }

}
