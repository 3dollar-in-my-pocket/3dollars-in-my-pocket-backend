package com.depromeet.threedollar.admin.config.interceptor

import com.depromeet.threedollar.admin.config.session.SessionConstants.ADMIN_ID
import com.depromeet.threedollar.common.exception.model.UnAuthorizedException
import com.depromeet.threedollar.domain.user.domain.admin.AdminRepository
import org.springframework.http.HttpHeaders
import org.springframework.session.Session
import org.springframework.session.SessionRepository
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthInterceptor(
    private val sessionRepository: SessionRepository<out Session?>,
    private val adminRepository: AdminRepository
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod) {
            return true
        }
        handler.getMethodAnnotation(Auth::class.java) ?: return true

        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (StringUtils.hasText(header) && header.startsWith(TOKEN_PREFIX)) {
            val sessionId = header.split(TOKEN_PREFIX)[1]
            val session = findSessionBySessionId(sessionId)

            val admin = adminRepository.findAdminById(session.getAttribute(ADMIN_ID))
                ?: throw UnAuthorizedException("잘못된 세션 id(${sessionId})입니다 다시 로그인해주세요.")

            request.setAttribute(ADMIN_ID, admin.id)
            return true
        }
        throw UnAuthorizedException("잘못된 토큰(${header})입니다 다시 로그인해주세요.")
    }

    private fun findSessionBySessionId(sessionId: String): Session {
        return sessionRepository.findById(sessionId)
            ?: throw UnAuthorizedException("잘못된 세션 $sessionId 입니다.")
    }

    companion object {
        const val TOKEN_PREFIX = "Bearer "
    }

}
