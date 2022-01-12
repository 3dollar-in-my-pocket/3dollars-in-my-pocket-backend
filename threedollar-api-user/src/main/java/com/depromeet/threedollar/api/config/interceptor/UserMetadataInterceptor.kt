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
class UserMetadataInterceptor : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val userAgent = request.getHeader(USER_AGENT_HEADER)
        val platform = OsPlatformType.findByUserAgent(userAgent)

        UserMetaSessionUtils.set(UserMetaValue(
            osPlatform = platform,
            userAgent = userAgent,
            sourceIp = request.getHeader(SOURCE_IP_HEADER),
            appVersion = extractAppVersion(platform, request)
        ))
        return true
    }

    private fun extractAppVersion(platform: OsPlatformType, request: HttpServletRequest): String? {
        return when (platform) {
            OsPlatformType.IPHONE -> request.getHeader(USER_AGENT_HEADER).split(IOS_USER_AGENT_POSTFIX)[0]
            OsPlatformType.ANDROID -> request.getHeader(ANDROID_VERSION_HEADER)
            else -> null
        }
    }

    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {
        UserMetaSessionUtils.remove()
    }

    companion object {
        private const val USER_AGENT_HEADER = "User-Agent"
        private const val SOURCE_IP_HEADER = "X-Forwarded-For"
        private const val ANDROID_VERSION_HEADER = "X-Android-Service-Version"

        private const val IOS_USER_AGENT_POSTFIX = " (com.macgongmon"
    }

}
