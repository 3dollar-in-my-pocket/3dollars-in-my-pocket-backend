package com.depromeet.threedollar.boss.api.config.interceptor

import com.depromeet.threedollar.boss.api.config.resolver.Auth
import com.depromeet.threedollar.common.exception.model.UnAuthorizedException
import com.depromeet.threedollar.boss.api.config.session.SessionConstants.BOSS_ACCOUNT_ID
import com.depromeet.threedollar.document.boss.document.account.BossAccountRepository
import org.springframework.http.HttpHeaders
import org.springframework.session.Session
import org.springframework.session.SessionRepository
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthInterceptor(
    private val sessionRepository: SessionRepository<out Session?>,
    private val bossAccountRepository: BossAccountRepository
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod) {
            return true
        }
        handler.getMethodAnnotation(Auth::class.java) ?: return true

        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
            ?: throw UnAuthorizedException("빈 Authorization 헤더입니다 다시 로그인해주세요.")

        if (!header.startsWith(HEADER_BEARER_PREFIX)) {
            throw UnAuthorizedException("빈 Authorization 헤더입니다 다시 로그인해주세요.")
        }

        val sessionId = header.split(HEADER_BEARER_PREFIX)[1]
        val bossAccount = bossAccountRepository.findBossAccountById(findSessionBySessionId(sessionId).getAttribute(BOSS_ACCOUNT_ID))
            ?: throw UnAuthorizedException("잘못된 세션 (${sessionId} 입니다 다시 로그인해주세요")

        request.setAttribute(BOSS_ACCOUNT_ID, bossAccount.id)
        return true
    }

    private fun findSessionBySessionId(sessionId: String): Session {
        return sessionRepository.findById(sessionId)
            ?: throw UnAuthorizedException("잘못된 세션 $sessionId 입니다.")
    }

    companion object {
        const val HEADER_BEARER_PREFIX = "Bearer "
    }

}
