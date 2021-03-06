package com.depromeet.threedollar.api.bossservice.config.interceptor

import com.depromeet.threedollar.api.bossservice.config.session.SessionConstants
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.model.UnAuthorizedException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistration
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationRepository
import org.springframework.http.HttpHeaders
import org.springframework.session.Session
import org.springframework.session.SessionRepository
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

private const val HEADER_BEARER_PREFIX = "Bearer "

@Component
class LoginCheckHandler(
    private val sessionRepository: SessionRepository<out Session?>,
    private val bossAccountRepository: BossAccountRepository,
    private val bossRegistrationRepository: BossRegistrationRepository,
) {

    fun checkAuthOptional(request: HttpServletRequest): Boolean {
        val authorization = request.getHeader(HttpHeaders.AUTHORIZATION) ?: return true
        if (!authorization.startsWith(HEADER_BEARER_PREFIX)) {
            return true
        }
        val sessionId: Session? = sessionRepository.findById(authorization.split(HEADER_BEARER_PREFIX)[1])
        val bossAccountId: String = sessionId?.getAttribute(SessionConstants.BOSS_ACCOUNT_ID) ?: return true
        if (bossAccountRepository.existsBossAccountById(bossAccountId)) {
            request.setAttribute(SessionConstants.BOSS_ACCOUNT_ID, bossAccountId)
        }
        return true
    }

    fun checkAuthOptionalAllowedWaiting(request: HttpServletRequest): Boolean {
        val authorization = request.getHeader(HttpHeaders.AUTHORIZATION) ?: return true
        if (!authorization.startsWith(HEADER_BEARER_PREFIX)) {
            return true
        }
        val sessionId: Session? = sessionRepository.findById(authorization.split(HEADER_BEARER_PREFIX)[1])
        val bossAccountId: String = sessionId?.getAttribute(SessionConstants.BOSS_ACCOUNT_ID) ?: return true

        if (bossAccountRepository.existsBossAccountById(bossAccountId)) {
            request.setAttribute(SessionConstants.BOSS_ACCOUNT_ID, bossAccountId)
            return true
        }

        val bossRegistration: BossRegistration? = bossRegistrationRepository.findWaitingRegistrationById(bossAccountId)
        if (bossRegistration != null) {
            request.setAttribute(SessionConstants.BOSS_ACCOUNT_ID, bossAccountId)
        }
        return true
    }

    fun checkAuthRequiredAllowedWaiting(request: HttpServletRequest): Boolean {
        val header: String? = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (header.isNullOrBlank() || !header.startsWith(HEADER_BEARER_PREFIX)) {
            throw UnAuthorizedException("????????? ????????????????????? - ($HEADER_BEARER_PREFIX) ????????? ?????? Authorization ??????($header)?????????.")
        }
        val sessionId: String = header.split(HEADER_BEARER_PREFIX)[1]
        val session: Session = findSessionBySessionId(sessionId)
        val bossAccountId: String = session.getAttribute(SessionConstants.BOSS_ACCOUNT_ID)
            ?: throw UnAuthorizedException("????????? ????????????????????? - ??????($sessionId)??? BOSS_ACCOUNT_ID??? ???????????? ????????????")

        if (bossAccountRepository.existsBossAccountById(bossAccountId)) {
            request.setAttribute(SessionConstants.BOSS_ACCOUNT_ID, bossAccountId)
            return true
        }

        if (bossRegistrationRepository.existsWaitingRegistrationById(bossAccountId)) {
            request.setAttribute(SessionConstants.BOSS_ACCOUNT_ID, bossAccountId)
            return true
        }
        throw UnAuthorizedException("???????????? ????????? ??????($bossAccountId) ?????? ???????????? ?????? ??????($bossAccountId)??? ???????????? ????????????")
    }

    fun checkAuthRequired(request: HttpServletRequest): Boolean {
        val header: String? = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (header.isNullOrBlank() || !header.startsWith(HEADER_BEARER_PREFIX)) {
            throw UnAuthorizedException("????????? ????????????????????? - ($HEADER_BEARER_PREFIX) ????????? ?????? Authorization ??????($header)?????????.")
        }
        val sessionId: String = header.split(HEADER_BEARER_PREFIX)[1]
        val session: Session = findSessionBySessionId(sessionId)
        val bossAccountId: String = session.getAttribute(SessionConstants.BOSS_ACCOUNT_ID)
            ?: throw UnAuthorizedException("????????? ????????????????????? - ??????($sessionId)??? BOSS_ACCOUNT_ID??? ???????????? ????????????")

        if (bossAccountRepository.existsBossAccountById(bossAccountId)) {
            request.setAttribute(SessionConstants.BOSS_ACCOUNT_ID, bossAccountId)
            return true
        }

        if (bossRegistrationRepository.existsWaitingRegistrationById(bossAccountId)) {
            throw ForbiddenException("?????? ?????? ?????? ???????????? ?????????($bossAccountId) ?????????", ErrorCode.E403_FORBIDDEN_WAITING_STATUS_TO_APPROVE_BOSS_ACCOUNT)
        }
        throw UnAuthorizedException("???????????? ????????? ??????($bossAccountId) ?????? ???????????? ?????? ??????($bossAccountId)??? ???????????? ????????????")
    }

    private fun findSessionBySessionId(sessionId: String): Session {
        return sessionRepository.findById(sessionId)
            ?: throw UnAuthorizedException("????????? ????????????????????? - ???????????? ??????($sessionId)??? ???????????? ????????????.")
    }

}
