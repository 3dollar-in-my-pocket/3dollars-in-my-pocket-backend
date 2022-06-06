package com.depromeet.threedollar.api.bossservice.service.auth

import org.springframework.stereotype.Service
import com.depromeet.threedollar.api.bossservice.service.account.BossAccountServiceUtils
import com.depromeet.threedollar.api.bossservice.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.common.utils.HttpHeaderUtils
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType.NAVER
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationRepository
import com.depromeet.threedollar.external.client.naver.NaverAuthApiClient
import com.depromeet.threedollar.external.client.naver.dto.response.NaverProfileResponse

private val SOCIAL_TYPE = NAVER

@Service
class NaverAuthService(
    private val bossAccountRepository: BossAccountRepository,
    private val bossRegistrationRepository: BossRegistrationRepository,
    private val naverAuthApiClient: NaverAuthApiClient,
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
        val profileInfoResponse: NaverProfileResponse = naverAuthApiClient.getProfileInfo(HttpHeaderUtils.withBearerToken(token))
        return profileInfoResponse.response.id
    }

}
