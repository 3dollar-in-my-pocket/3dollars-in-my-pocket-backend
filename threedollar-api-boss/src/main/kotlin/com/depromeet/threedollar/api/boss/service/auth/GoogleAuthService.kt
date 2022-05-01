package com.depromeet.threedollar.api.boss.service.auth

import org.springframework.stereotype.Service
import com.depromeet.threedollar.api.boss.service.account.BossAccountServiceUtils
import com.depromeet.threedollar.api.boss.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.common.utils.HttpHeaderUtils
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.BossBossRegistrationRepository
import com.depromeet.threedollar.external.client.google.GoogleAuthApiClient

private val SOCIAL_TYPE = BossAccountSocialType.GOOGLE

@Service
class GoogleAuthService(
    private val bossAccountRepository: BossAccountRepository,
    private val bossRegistrationRepository: BossBossRegistrationRepository,
    private val googleAuthApiClient: GoogleAuthApiClient
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
        return googleAuthApiClient.getProfileInfo(HttpHeaderUtils.withBearerToken(token)).id
    }

}
