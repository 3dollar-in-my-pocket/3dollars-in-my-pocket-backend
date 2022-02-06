package com.depromeet.threedollar.boss.api.service.auth

import com.depromeet.threedollar.boss.api.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.boss.api.service.auth.policy.NaverAuthService
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.document.boss.document.account.BossAccountCreator
import com.depromeet.threedollar.document.boss.document.account.BossAccountRepository
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import com.depromeet.threedollar.external.client.naver.NaverAuthApiClient
import com.depromeet.threedollar.external.client.naver.dto.response.NaverProfileInfoResponse
import com.depromeet.threedollar.external.client.naver.dto.response.NaverProfileResponse
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
class NaverAuthServiceTest(
    private val bossAccountRepository: BossAccountRepository
) {

    private lateinit var authService: AuthService

    @BeforeEach
    fun setUp() {
        authService = NaverAuthService(bossAccountRepository, StubNaverAuthApiClient())
    }

    @AfterEach
    fun cleanUp() {
        bossAccountRepository.deleteAll()
    }

    @Test
    fun `네이버 로그인이 성공하면 ID가 반환된다`() {
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
    fun `네이버 로그인시 가입한 유저가 아니면 404 에러 발생`() {
        // when & then
        Assertions.assertThatThrownBy { authService.login(LoginRequest("token", SOCIAL_TYPE)) }.isInstanceOf(NotFoundException::class.java)
    }

    private class StubNaverAuthApiClient : NaverAuthApiClient {
        override fun getProfileInfo(accessToken: String?): NaverProfileResponse {
            return NaverProfileResponse(
                NaverProfileInfoResponse(id = SOCIAL_ID)
            )
        }
    }

    companion object {
        private const val SOCIAL_ID = "social-id"
        private val SOCIAL_TYPE = BossAccountSocialType.NAVER
    }

}
