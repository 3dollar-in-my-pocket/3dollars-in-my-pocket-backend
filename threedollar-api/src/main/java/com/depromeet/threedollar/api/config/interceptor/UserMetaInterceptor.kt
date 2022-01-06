package com.depromeet.threedollar.api.config.interceptor

import com.depromeet.threedollar.common.model.UserMetaValue
import com.depromeet.threedollar.common.type.OsPlatformType
import com.depromeet.threedollar.common.utils.UserMetaSessionUtils
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class UserMetaInterceptor : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val userAgent = request.getHeader(USER_AGENT_HEADER)
        val sourceIp = request.getHeader(SOURCE_IP_HEADER)
        UserMetaSessionUtils.set(UserMetaValue(
            osPlatform = OsPlatformType.findByUserAgent(userAgent),
            userAgent = userAgent,
            sourceIp = sourceIp
        ))
        return true
    }

    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {
        UserMetaSessionUtils.remove()
    }

    companion object {
        private const val USER_AGENT_HEADER = "User-Agent"
        private const val SOURCE_IP_HEADER = "X-Forwarded-For"
    }

}
