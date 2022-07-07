package com.depromeet.threedollar.api.bossservice.service.auth

import com.depromeet.threedollar.api.bossservice.service.account.BossAccountServiceHelper
import com.depromeet.threedollar.api.bossservice.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType.APPLE
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationRepository
import com.depromeet.threedollar.infrastructure.external.client.apple.AppleTokenDecoder
import org.springframework.stereotype.Service

private val SOCIAL_TYPE = APPLE

@Service
class AppleAuthService(
    private val bossAccountRepository: BossAccountRepository,
    private val bossRegistrationRepository: BossRegistrationRepository,
    private val appleTokenDecoder: AppleTokenDecoder,
) : AuthService {

    override fun login(request: LoginRequest): String {
        return BossAccountServiceHelper.findBossAccountBySocialIdAndSocialTypeWithCheckWaitingRegistration(
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
