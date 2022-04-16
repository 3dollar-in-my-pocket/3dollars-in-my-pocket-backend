package com.depromeet.threedollar.api.boss.config.interceptor

import com.depromeet.threedollar.api.boss.config.session.SessionConstants.BOSS_ACCOUNT_ID
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.model.UnAuthorizedException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationRepository
import org.springframework.http.HttpHeaders
import org.springframework.session.Session
import org.springframework.session.SessionRepository
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private const val HEADER_BEARER_PREFIX = "Bearer "

@Component
class AuthInterceptor(
    private val sessionRepository: SessionRepository<out Session?>,
    private val bossAccountRepository: BossAccountRepository,
    private val registrationRepository: RegistrationRepository
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod) {
            return true
        }
        val auth = handler.getMethodAnnotation(Auth::class.java) ?: return true

        if (auth.optional) {
            return optionalAuth(request)
        }
        return requiredAuth(request)
    }

    private fun optionalAuth(request: HttpServletRequest): Boolean {
        val authorization = request.getHeader(HttpHeaders.AUTHORIZATION) ?: return true
        if (authorization.split(HEADER_BEARER_PREFIX).size <= 1) {
            return true
        }
        val sessionId = sessionRepository.findById(authorization.split(HEADER_BEARER_PREFIX)[1])
        val bossAccountId: String? = sessionId?.getAttribute(BOSS_ACCOUNT_ID)
        bossAccountId?.let {
            if (bossAccountRepository.existsBossAccountById(bossAccountId)) {
                request.setAttribute(BOSS_ACCOUNT_ID, bossAccountId)
            }
        }
        return true
    }

    private fun requiredAuth(request: HttpServletRequest): Boolean {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
            ?: throw UnAuthorizedException("빈 Authorization 헤더입니다 다시 로그인해주세요.")

        if (!header.startsWith(HEADER_BEARER_PREFIX)) {
            throw UnAuthorizedException("빈 Authorization 헤더입니다 다시 로그인해주세요.")
        }

        val sessionId = header.split(HEADER_BEARER_PREFIX)[1]
        val bossAccountId: String = findSessionBySessionId(sessionId).getAttribute(BOSS_ACCOUNT_ID)

        if (bossAccountRepository.existsBossAccountById(bossAccountId)) {
            request.setAttribute(BOSS_ACCOUNT_ID, bossAccountId)
            return true
        }
        registrationRepository.findWaitingRegistrationById(bossAccountId)?.let {
            throw ForbiddenException("현재 가입 승인 대기중인 사장님 ($bossAccountId) 입니다", ErrorCode.FORBIDDEN_WAITING_APPROVE_BOSS_ACCOUNT)
        } ?: throw UnAuthorizedException("해당하는 등록 번호($bossAccountId)를 가진 계정 혹은 대기중인 가입 신청은 존재하지 않습니다")
    }

    private fun findSessionBySessionId(sessionId: String): Session {
        return sessionRepository.findById(sessionId)
            ?: throw UnAuthorizedException("잘못된 세션 $sessionId 입니다.")
    }

}
