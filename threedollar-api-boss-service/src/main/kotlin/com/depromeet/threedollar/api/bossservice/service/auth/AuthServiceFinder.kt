package com.depromeet.threedollar.api.bossservice.service.auth

import com.depromeet.threedollar.common.exception.model.ServiceUnAvailableException
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct

private val authServiceMap: MutableMap<BossAccountSocialType, AuthService> = EnumMap(
    BossAccountSocialType::class.java)

@Component
class AuthServiceFinder(
    private val kaKaoAuthService: KaKaoAuthService,
    private val appleAuthService: AppleAuthService,
    private val googleAuthService: GoogleAuthService,
) {

    @PostConstruct
    fun initializeAuthServicesMap() {
        authServiceMap[BossAccountSocialType.KAKAO] = kaKaoAuthService
        authServiceMap[BossAccountSocialType.APPLE] = appleAuthService
        authServiceMap[BossAccountSocialType.GOOGLE] = googleAuthService
    }

    fun getAuthService(socialType: BossAccountSocialType): AuthService {
        return authServiceMap[socialType]
            ?: throw ServiceUnAvailableException("AuthService ($socialType) 로직이 구현되지 않았습니다")
    }

}
