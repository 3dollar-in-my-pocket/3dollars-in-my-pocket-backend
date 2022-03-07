package com.depromeet.threedollar.api.boss.service.auth

import com.depromeet.threedollar.api.boss.service.auth.policy.AppleAuthService
import com.depromeet.threedollar.api.boss.service.auth.policy.GoogleAuthService
import com.depromeet.threedollar.api.boss.service.auth.policy.KaKaoAuthService
import com.depromeet.threedollar.api.boss.service.auth.policy.NaverAuthService
import com.depromeet.threedollar.common.exception.model.InternalServerException
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct

private val authServiceMap: MutableMap<BossAccountSocialType, AuthService> = EnumMap(BossAccountSocialType::class.java)

@Component
class AuthServiceProvider(
    private val kaKaoAuthService: KaKaoAuthService,
    private val appleAuthService: AppleAuthService,
    private val googleAuthService: GoogleAuthService,
    private val naverAuthService: NaverAuthService
) {

    @PostConstruct
    fun initializeAuthServicesMap() {
        authServiceMap[BossAccountSocialType.KAKAO] = kaKaoAuthService
        authServiceMap[BossAccountSocialType.APPLE] = appleAuthService
        authServiceMap[BossAccountSocialType.GOOGLE] = googleAuthService
        authServiceMap[BossAccountSocialType.NAVER] = naverAuthService
    }

    fun getAuthService(socialType: BossAccountSocialType): AuthService {
        return authServiceMap[socialType] ?: throw InternalServerException("AuthService ($socialType) 로직이 구현되지 않았습니다")
    }

}
