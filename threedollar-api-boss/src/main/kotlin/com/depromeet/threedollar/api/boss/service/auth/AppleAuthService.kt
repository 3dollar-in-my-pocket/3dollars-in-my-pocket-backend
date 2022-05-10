package com.depromeet.threedollar.api.boss.service.auth

import org.springframework.stereotype.Service
import com.depromeet.threedollar.api.boss.service.account.BossAccountServiceUtils
import com.depromeet.threedollar.api.boss.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType.APPLE
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.BossRegistrationRepository
import com.depromeet.threedollar.external.client.apple.AppleTokenDecoder

private val SOCIAL_TYPE = APPLE

@Service
class AppleAuthService(
    private val bossAccountRepository: BossAccountRepository,
    private val bossRegistrationRepository: BossRegistrationRepository,
    private val appleTokenDecoder: AppleTokenDecoder
) : AuthService {

    override fun login(request: LoginRequest): String {
        return BossAccountServiceUtils.findBossAccountBySocialIdAndSocialTypeWithCheckWaitingRegistration(
            bossAccountRepository = bossAccountRepository,
            bossRegistrationRepository = bossRegistrationRepository,
            socialId = getSocialId(request.token),
            socialType = SOCIAL_TYPE
        )
    }

    override fun getSocialId(token: String): String {
        return appleTokenDecoder.getSocialIdFromIdToken(token)
    }

}
