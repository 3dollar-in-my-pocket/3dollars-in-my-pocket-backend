package com.depromeet.threedollar.boss.api.service.auth.policy

import com.depromeet.threedollar.boss.api.service.account.BossAccountServiceUtils
import com.depromeet.threedollar.boss.api.service.auth.AuthService
import com.depromeet.threedollar.boss.api.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.common.utils.HttpHeaderUtils
import com.depromeet.threedollar.document.boss.document.account.BossAccountRepository
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import com.depromeet.threedollar.document.boss.document.registration.RegistrationRepository
import com.depromeet.threedollar.external.client.naver.NaverAuthApiClient
import org.springframework.stereotype.Service

@Service
class NaverAuthService(
    private val bossAccountRepository: BossAccountRepository,
    private val registrationRepository: RegistrationRepository,
    private val naverAuthApiClient: NaverAuthApiClient
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
        return naverAuthApiClient.getProfileInfo(HttpHeaderUtils.withBearerToken(request.token)).response.id
    }

    companion object {
        private val SOCIAL_TYPE = BossAccountSocialType.NAVER
    }

}
