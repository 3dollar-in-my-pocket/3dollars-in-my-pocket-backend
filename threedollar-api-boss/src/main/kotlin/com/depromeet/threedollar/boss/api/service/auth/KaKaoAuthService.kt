package com.depromeet.threedollar.boss.api.service.auth

import com.depromeet.threedollar.boss.api.service.account.findBossAccountBySocialIdAndSocialType
import com.depromeet.threedollar.boss.api.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.document.boss.document.account.BossAccountRepository
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import com.depromeet.threedollar.external.client.kakao.KaKaoAuthApiClient
import org.springframework.stereotype.Service

@Service
class KaKaoAuthService(
    private val bossAccountRepository: BossAccountRepository,
    private val kaKaoAuthApiClient: KaKaoAuthApiClient
) : AuthService {

    override fun login(request: LoginRequest): String {
        val kaKaoProfile = kaKaoAuthApiClient.getProfileInfo(request.token)
        return findBossAccountBySocialIdAndSocialType(bossAccountRepository, kaKaoProfile.id, SOCIAL_TYPE).id
    }

    companion object {
        private val SOCIAL_TYPE = BossAccountSocialType.KAKAO
    }

}
