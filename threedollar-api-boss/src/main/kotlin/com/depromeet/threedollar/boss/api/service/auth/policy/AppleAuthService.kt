package com.depromeet.threedollar.boss.api.service.auth.policy

import com.depromeet.threedollar.boss.api.service.account.BossAccountServiceUtils
import com.depromeet.threedollar.boss.api.service.auth.AuthService
import com.depromeet.threedollar.boss.api.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.document.boss.document.account.BossAccountRepository
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import com.depromeet.threedollar.external.client.apple.AppleTokenDecoder
import org.springframework.stereotype.Service

@Service
class AppleAuthService(
    private val bossAccountRepository: BossAccountRepository,
    private val appleTokenDecoder: AppleTokenDecoder
) : AuthService {

    override fun login(request: LoginRequest): String {
        val appleSocialId = findSocialId(request)
        return BossAccountServiceUtils.findBossAccountBySocialIdAndSocialType(bossAccountRepository, appleSocialId, SOCIAL_TYPE).id
    }

    override fun findSocialId(request: LoginRequest): String {
        return appleTokenDecoder.getSocialIdFromIdToken(request.token)
    }

    companion object {
        private val SOCIAL_TYPE = BossAccountSocialType.APPLE
    }

}
