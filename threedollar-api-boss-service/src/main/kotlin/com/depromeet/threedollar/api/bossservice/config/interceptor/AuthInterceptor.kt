package com.depromeet.threedollar.api.bossservice.config.interceptor

import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthInterceptor(
    private val loginCheckHandler: LoginCheckHandler,
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod) {
            return true
        }
        val auth = handler.getMethodAnnotation(Auth::class.java) ?: return true
        return when (auth.optional) {
            true -> {
                when (auth.allowedWaiting) {
                    true -> loginCheckHandler.checkAuthOptionalAllowedWaiting(request)
                    false -> loginCheckHandler.checkAuthOptional(request)
                }
            }
            false -> {
                when (auth.allowedWaiting) {
                    true -> loginCheckHandler.checkAuthRequiredAllowedWaiting(request)
                    false -> loginCheckHandler.checkAuthRequired(request)
                }
            }
        }
    }

}
