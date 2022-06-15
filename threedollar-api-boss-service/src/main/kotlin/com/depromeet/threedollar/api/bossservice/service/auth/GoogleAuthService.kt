package com.depromeet.threedollar.api.bossservice.service.auth

import org.springframework.stereotype.Service
import com.depromeet.threedollar.api.bossservice.service.account.BossAccountServiceHelper
import com.depromeet.threedollar.api.bossservice.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.common.utils.HttpHeaderUtils
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType.GOOGLE
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationRepository
import com.depromeet.threedollar.external.client.google.GoogleAuthApiClient
import com.depromeet.threedollar.external.client.google.dto.response.GoogleProfileInfoResponse

private val SOCIAL_TYPE = GOOGLE

@Service
class GoogleAuthService(
    private val bossAccountRepository: BossAccountRepository,
    private val bossRegistrationRepository: BossRegistrationRepository,
    private val googleAuthApiClient: GoogleAuthApiClient,
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
        val profileInfoResponse: GoogleProfileInfoResponse = googleAuthApiClient.getProfileInfo(HttpHeaderUtils.withBearerToken(token))
        return profileInfoResponse.id
    }

}
