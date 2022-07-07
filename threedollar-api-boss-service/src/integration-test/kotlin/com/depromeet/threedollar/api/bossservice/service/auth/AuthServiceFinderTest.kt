package com.depromeet.threedollar.api.bossservice.service.auth

import com.depromeet.threedollar.api.bossservice.IntegrationTest
import com.depromeet.threedollar.common.exception.model.ServiceUnAvailableException
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class AuthServiceFinderTest(
    private val authServiceFinder: AuthServiceFinder,
) : IntegrationTest() {

    @MethodSource("bossSocialTypeAndServices")
    @ParameterizedTest
    fun `각 소셜타입의 AuthService를 가져온다`(socialType: BossAccountSocialType, expectedAuthServiceClass: Class<AuthService>) {
        // when
        val authService = authServiceFinder.getAuthService(socialType)

        // then
        assertThat(authService).isInstanceOf(expectedAuthServiceClass)
    }

    companion object {
        @JvmStatic
        private fun bossSocialTypeAndServices(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(BossAccountSocialType.KAKAO, KaKaoAuthService::class.java),
                Arguments.of(BossAccountSocialType.APPLE, AppleAuthService::class.java),
                Arguments.of(BossAccountSocialType.GOOGLE, GoogleAuthService::class.java)
            )
        }
    }

    @Test
    fun `네이버 인증방식은 아직 지원하지 않는다`() {
        // given
        val socialType = BossAccountSocialType.NAVER

        // when & then
        assertThatThrownBy { authServiceFinder.getAuthService(socialType) }.isInstanceOf(ServiceUnAvailableException::class.java)
    }

}
