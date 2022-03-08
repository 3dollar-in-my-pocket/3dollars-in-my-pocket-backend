package com.depromeet.threedollar.api.boss.config.resolver

import com.depromeet.threedollar.common.exception.model.InternalServerException
import com.depromeet.threedollar.api.boss.config.session.SessionConstants.BOSS_ACCOUNT_ID
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Configuration
class BossIdResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(BossId::class.java) && parameter.parameterType == String::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        return webRequest.getAttribute(BOSS_ACCOUNT_ID, 0)
            ?: throw InternalServerException("bossId를 받아오지 못했습니다.")
    }

}