package com.depromeet.threedollar.api.bossservice.config.interceptor

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import com.depromeet.threedollar.common.model.UserMetaValue
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.common.type.OsPlatformType
import com.depromeet.threedollar.common.utils.ClientIpUtils
import com.depromeet.threedollar.common.utils.TraceIdUtils
import com.depromeet.threedollar.common.utils.UserMetaSessionUtils

private const val USER_AGENT_HEADER = "User-Agent"
private const val X_FORWARDED_FOR_HEADER = "X-Forwarded-For"
private const val ANDROID_VERSION_HEADER = "X-Android-Service-Version"
private const val IOS_USER_AGENT_POSTFIX = "(com.macgongmon"
private const val TRACE_ID = "X-Amzn-Trace-Id"

@Component
class UserMetaInterceptor : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val osPlatformType: OsPlatformType = getOsPlatform(request)
        UserMetaSessionUtils.set(UserMetaValue.of(
            osPlatform = osPlatformType,
            userAgent = request.getHeader(USER_AGENT_HEADER),
            clientIp = ClientIpUtils.getClientIp(request.remoteAddr, request.getHeader(X_FORWARDED_FOR_HEADER)),
            applicationType = ApplicationType.BOSS_API,
            appVersion = extractAppVersion(osPlatformType, request),
            traceId = request.getHeader(TRACE_ID) ?: TraceIdUtils.generate(),
        ))
        return true
    }

    private fun getOsPlatform(request: HttpServletRequest): OsPlatformType {
        if (request.getHeader(ANDROID_VERSION_HEADER) != null) {
            return OsPlatformType.ANDROID
        }
        return OsPlatformType.findByUserAgent(request.getHeader(USER_AGENT_HEADER))
    }

    private fun extractAppVersion(platform: OsPlatformType, request: HttpServletRequest): String? {
        return when (platform) {
            OsPlatformType.IPHONE -> request.getHeader(USER_AGENT_HEADER).split(IOS_USER_AGENT_POSTFIX)[0].trim()
            OsPlatformType.ANDROID -> request.getHeader(ANDROID_VERSION_HEADER)
            else -> null
        }
    }

    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {
        UserMetaSessionUtils.remove()
    }

}
