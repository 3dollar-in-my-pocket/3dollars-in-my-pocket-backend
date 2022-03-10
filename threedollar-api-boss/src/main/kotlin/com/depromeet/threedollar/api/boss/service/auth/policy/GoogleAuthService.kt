package com.depromeet.threedollar.api.boss.service.auth.policy

import com.depromeet.threedollar.api.boss.service.account.BossAccountServiceUtils
import com.depromeet.threedollar.api.boss.service.auth.AuthService
import com.depromeet.threedollar.api.boss.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.common.utils.HttpHeaderUtils
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationRepository
import com.depromeet.threedollar.external.client.google.GoogleAuthApiClient
import org.springframework.stereotype.Service

private val SOCIAL_TYPE = com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType.GOOGLE

@Service
class GoogleAuthService(
    private val bossAccountRepository: BossAccountRepository,
    private val registrationRepository: RegistrationRepository,
    private val googleAuthApiClient: GoogleAuthApiClient
) : AuthService {

    override fun login(request: LoginRequest): String {
        val bossAccount = BossAccountServiceUtils.findBossAccountBySocialIdAndSocialTypeWithCheckWaitingRegistration(
            bossAccountRepository,
            registrationRepository,
            getSocialId(request),
            SOCIAL_TYPE
        )
        return bossAccount.id
    }

    override fun getSocialId(request: LoginRequest): String {
        return googleAuthApiClient.getProfileInfo(HttpHeaderUtils.withBearerToken(request.token)).id
    }

}
