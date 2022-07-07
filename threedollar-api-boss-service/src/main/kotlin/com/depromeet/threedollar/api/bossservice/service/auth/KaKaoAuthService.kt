package com.depromeet.threedollar.api.bossservice.service.auth

import com.depromeet.threedollar.api.bossservice.service.account.BossAccountServiceHelper
import com.depromeet.threedollar.api.bossservice.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.common.utils.HttpHeaderUtils
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType.KAKAO
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationRepository
import com.depromeet.threedollar.infrastructure.external.client.kakao.KaKaoAuthApiClient
import com.depromeet.threedollar.infrastructure.external.client.kakao.dto.response.KaKaoProfileResponse
import org.springframework.stereotype.Service

private val SOCIAL_TYPE = KAKAO

@Service
class KaKaoAuthService(
    private val bossAccountRepository: BossAccountRepository,
    private val bossRegistrationRepository: BossRegistrationRepository,
    private val kaKaoAuthApiClient: KaKaoAuthApiClient,
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
        val profileInfoResponse: KaKaoProfileResponse = kaKaoAuthApiClient.getProfileInfo(HttpHeaderUtils.withBearerToken(token))
        return profileInfoResponse.id
    }

}
