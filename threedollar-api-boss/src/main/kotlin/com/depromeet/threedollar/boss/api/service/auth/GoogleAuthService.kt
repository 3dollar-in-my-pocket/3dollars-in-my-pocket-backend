package com.depromeet.threedollar.boss.api.service.auth

import com.depromeet.threedollar.boss.api.service.account.BossAccountServiceUtils
import com.depromeet.threedollar.boss.api.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.document.boss.document.account.BossAccountRepository
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import com.depromeet.threedollar.external.client.google.GoogleAuthApiClient
import org.springframework.stereotype.Service

@Service
class GoogleAuthService(
    private val bossAccountRepository: BossAccountRepository,
    private val googleAuthApiClient: GoogleAuthApiClient
) : AuthService {

    override fun login(request: LoginRequest): String {
        val googleProfile = googleAuthApiClient.getProfileInfo(request.token)
        return BossAccountServiceUtils.findBossAccountBySocialIdAndSocialType(
            bossAccountRepository,
            googleProfile.id,
            SOCIAL_TYPE
        ).id
    }

    override fun findSocialId(request: LoginRequest): String {
        return googleAuthApiClient.getProfileInfo(request.token).id
    }

    companion object {
        private val SOCIAL_TYPE = BossAccountSocialType.GOOGLE
    }

}
