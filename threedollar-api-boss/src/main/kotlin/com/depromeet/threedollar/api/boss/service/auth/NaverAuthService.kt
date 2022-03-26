package com.depromeet.threedollar.api.boss.service.auth

import com.depromeet.threedollar.api.boss.service.account.BossAccountServiceUtils
import com.depromeet.threedollar.api.boss.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.common.utils.HttpHeaderUtils
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationRepository
import com.depromeet.threedollar.external.client.naver.NaverAuthApiClient
import org.springframework.stereotype.Service

private val SOCIAL_TYPE = BossAccountSocialType.NAVER

@Service
class NaverAuthService(
    private val bossAccountRepository: BossAccountRepository,
    private val registrationRepository: RegistrationRepository,
    private val naverAuthApiClient: NaverAuthApiClient
) : AuthService {

    override fun login(request: LoginRequest): String {
        return BossAccountServiceUtils.findBossAccountBySocialIdAndSocialTypeWithCheckWaitingRegistration(
            bossAccountRepository = bossAccountRepository,
            registrationRepository = registrationRepository,
            socialId = getSocialId(request.token),
            socialType = SOCIAL_TYPE
        )
    }

    override fun getSocialId(token: String): String {
        return naverAuthApiClient.getProfileInfo(HttpHeaderUtils.withBearerToken(token)).response.id
    }

}
