package com.depromeet.threedollar.boss.api.config.interceptor

import com.depromeet.threedollar.boss.api.config.resolver.Auth
import com.depromeet.threedollar.boss.api.config.resolver.Role
import com.depromeet.threedollar.common.exception.model.UnAuthorizedException
import com.depromeet.threedollar.boss.api.config.session.SessionConstants.BOSS_ACCOUNT_ID
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.document.boss.document.account.BossAccountRepository
import com.depromeet.threedollar.document.boss.document.store.BossStoreRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpHeaders
import org.springframework.session.Session
import org.springframework.session.SessionRepository
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.HandlerMapping
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class AuthInterceptor(
    private val sessionRepository: SessionRepository<out Session?>,
    private val bossAccountRepository: BossAccountRepository,
    private val objectMapper: ObjectMapper,
    private val bossStoreRepository: BossStoreRepository
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod) {
            return true
        }
        val annotation = handler.getMethodAnnotation(Auth::class.java) ?: return true

        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (StringUtils.hasText(header) && header.startsWith(HEADER_BEARER_PREFIX)) {
            val sessionId = header.split(HEADER_BEARER_PREFIX)[1]
            val session = findSessionBySessionId(sessionId)

            val bossAccount = bossAccountRepository.findBossAccountById(session.getAttribute(BOSS_ACCOUNT_ID))
                ?: throw UnAuthorizedException("잘못된 세션 (${sessionId} 입니다 다시 로그인해주세요")

            request.setAttribute(BOSS_ACCOUNT_ID, bossAccount.id)

            if (annotation.role == Role.STORE_OWNER) {
                val pathVariables: Map<String, String> = objectMapper.convertValue(
                    request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE),
                    object : TypeReference<Map<String, String>>() {})
                val storeId = pathVariables["storeId"]
                    ?: throw IllegalArgumentException("해당하는 가게가 존재하지 않아요 혹은 파라미터로 storeId가 필요합니다")

                validateBossStoreOwner(bossStoreRepository, bossStoreId = storeId, bossId = bossAccount.id)
            }
            return true
        }
        throw UnAuthorizedException("잘못된 헤더(${header})입니다 다시 로그인해주세요.")
    }

    private fun validateBossStoreOwner(bossStoreRepository: BossStoreRepository, bossStoreId: String, bossId: String) {
        bossStoreRepository.findByIdAndBossId(bossStoreId, bossId)
            ?: throw ForbiddenException("해당하는 가게 (${bossStoreId}의 사장님이 아닙니다. bossId: ${bossId}")
    }

    private fun findSessionBySessionId(sessionId: String): Session {
        return sessionRepository.findById(sessionId)
            ?: throw UnAuthorizedException("잘못된 세션 $sessionId 입니다.")
    }

    companion object {
        const val HEADER_BEARER_PREFIX = "Bearer "
    }

}
