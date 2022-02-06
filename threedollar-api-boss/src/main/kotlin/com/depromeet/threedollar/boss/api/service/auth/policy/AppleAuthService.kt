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
        val bossAccount = BossAccountServiceUtils.findBossAccountBySocialIdAndSocialType(
            bossAccountRepository = bossAccountRepository,
            socialId = getSocialId(request),
            socialType = SOCIAL_TYPE
        )
        return bossAccount.id
    }

    override fun getSocialId(request: LoginRequest): String {
        return appleTokenDecoder.getSocialIdFromIdToken(request.token)
    }

    companion object {
        private val SOCIAL_TYPE = BossAccountSocialType.APPLE
    }

}
