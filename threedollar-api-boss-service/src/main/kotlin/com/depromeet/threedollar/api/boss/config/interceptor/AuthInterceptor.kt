package com.depromeet.threedollar.api.boss.config.interceptor

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthInterceptor(
    private val loginCheckHandler: LoginCheckHandler,
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod) {
            return true
        }
        val auth = handler.getMethodAnnotation(Auth::class.java) ?: return true
        if (auth.optional) {
            return loginCheckHandler.checkAuthOptional(request)
        }
        return loginCheckHandler.checkAuthRequired(request)
    }

}
