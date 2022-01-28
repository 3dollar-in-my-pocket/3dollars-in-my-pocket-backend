package com.depromeet.threedollar.boss.api.service.auth

import com.depromeet.threedollar.boss.api.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.boss.api.service.auth.policy.KaKaoAuthService
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.document.boss.document.account.*
import com.depromeet.threedollar.external.client.kakao.KaKaoAuthApiClient
import com.depromeet.threedollar.external.client.kakao.dto.response.KaKaoProfileResponse
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class KaKaoAuthServiceTest(
    private val bossAccountRepository: BossAccountRepository
) {

    private lateinit var authService: AuthService

    @BeforeEach
    fun setUp() {
        authService = KaKaoAuthService(bossAccountRepository, StubKaKaoApiClient())
    }

    @AfterEach
    fun cleanUp() {
        bossAccountRepository.deleteAll()
    }

    @Test
    fun `카카오 로그인이 성공하면 ID가 반환된다`() {
        // given
        val bossAccount = BossAccountCreator.create(
            name = "사장님",
            socialId = SOCIAL_ID,
            socialType = SOCIAL_TYPE
        )
        bossAccountRepository.save(bossAccount)

        // when
        val accountId = authService.login(LoginRequest("token", SOCIAL_TYPE))

        // then
        Assertions.assertThat(accountId).isEqualTo(bossAccount.id)
    }

    @Test
    fun `카카오 로그인시 가입한 유저가 아니면 404 에러 발생`() {
        // when & then
        Assertions.assertThatThrownBy { authService.login(LoginRequest("token", SOCIAL_TYPE)) }.isInstanceOf(NotFoundException::class.java)
    }

    private class StubKaKaoApiClient : KaKaoAuthApiClient {
        override fun getProfileInfo(accessToken: String?): KaKaoProfileResponse {
            return KaKaoProfileResponse.testInstance(SOCIAL_ID)
        }
    }

    companion object {
        private const val SOCIAL_ID = "social-id"
        private val SOCIAL_TYPE = BossAccountSocialType.KAKAO
    }

}
