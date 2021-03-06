package com.depromeet.threedollar.api.adminservice.config.interceptor

import com.depromeet.threedollar.api.adminservice.config.session.SessionConstants.ADMIN_ID
import com.depromeet.threedollar.common.exception.model.UnAuthorizedException
import com.depromeet.threedollar.domain.rds.domain.commonservice.admin.AdminRepository
import org.springframework.http.HttpHeaders
import org.springframework.session.Session
import org.springframework.session.SessionRepository
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private const val TOKEN_PREFIX = "Bearer "

@Component
class AuthInterceptor(
    private val sessionRepository: SessionRepository<out Session?>,
    private val adminRepository: AdminRepository,
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod) {
            return true
        }
        handler.getMethodAnnotation(Auth::class.java) ?: return true

        val header: String? = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (header.isNullOrBlank() || !header.startsWith(TOKEN_PREFIX)) {
            throw UnAuthorizedException("인증이 실패하였습니다 - 비거나 ($TOKEN_PREFIX) 형식이 아닌 헤더(${header})가 요청되었습니다.")
        }

        val sessionId: String = header.split(TOKEN_PREFIX)[1]
        val session: Session = findSessionBySessionId(sessionId)

        val adminId: Long = session.getAttribute(ADMIN_ID)
            ?: throw UnAuthorizedException("인증이 실패하였습니다 - 세션($sessionId)에 ADMIN_ID가 존재하지 않습니다")

        validateExistsAdminId(adminId)

        request.setAttribute(ADMIN_ID, adminId)
        return true
    }

    private fun findSessionBySessionId(sessionId: String): Session {
        return sessionRepository.findById(sessionId)
            ?: throw UnAuthorizedException("인증이 실패하였습니다 - 해당하는 세션($sessionId)은 존재하지 않습니다")
    }

    private fun validateExistsAdminId(adminId: Long) {
        if (!adminRepository.existsAdminById(adminId)) {
            throw UnAuthorizedException("인증이 실패하였습니다 - 해당하는 관리자(${adminId})가 존재하지 않습니다.")
        }
    }

}
