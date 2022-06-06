package com.depromeet.threedollar.api.boss.service.auth

import java.util.*
import javax.annotation.PostConstruct
import org.springframework.stereotype.Component
import com.depromeet.threedollar.common.exception.model.ServiceUnAvailableException

private val authServiceMap: MutableMap<com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType, AuthService> = EnumMap(
    com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType::class.java)

@Component
class AuthServiceFinder(
    private val kaKaoAuthService: KaKaoAuthService,
    private val appleAuthService: AppleAuthService,
    private val googleAuthService: GoogleAuthService,
) {

    @PostConstruct
    fun initializeAuthServicesMap() {
        authServiceMap[com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType.KAKAO] = kaKaoAuthService
        authServiceMap[com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType.APPLE] = appleAuthService
        authServiceMap[com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType.GOOGLE] = googleAuthService
    }

    fun getAuthService(socialType: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType): AuthService {
        return authServiceMap[socialType]
            ?: throw ServiceUnAvailableException("AuthService ($socialType) 로직이 구현되지 않았습니다")
    }

}
