package com.depromeet.threedollar.api.boss.service.auth

import com.depromeet.threedollar.api.boss.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.api.boss.service.auth.policy.NaverAuthService
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationRepository
import com.depromeet.threedollar.external.client.naver.NaverAuthApiClient
import com.depromeet.threedollar.external.client.naver.dto.response.NaverProfileInfoResponse
import com.depromeet.threedollar.external.client.naver.dto.response.NaverProfileResponse
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

private const val SOCIAL_ID = "social-id"
private val SOCIAL_TYPE = BossAccountSocialType.NAVER

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class NaverAuthServiceTest(
    private val bossAccountRepository: BossAccountRepository,
    private val registrationRepository: RegistrationRepository
) {

    private lateinit var authService: AuthService

    @BeforeEach
    fun setUp() {
        authService = NaverAuthService(bossAccountRepository, registrationRepository, StubNaverAuthApiClient())
    }

    @AfterEach
    fun cleanUp() {
        registrationRepository.deleteAll()
        bossAccountRepository.deleteAll()
    }

    @Test
    fun `네이버 로그인이 성공하면 사장님 계정의 ID가 반환된다`() {
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
        Assertions.assertThatThrownBy {
            authService.login(LoginRequest(token = "token", socialType = SOCIAL_TYPE))
        }.isInstanceOf(NotFoundException::class.java)
    }

    @Test
    fun `네이버 로그인시 가입 승인 대기중인 유저면 403 Forbidden 에러 발생`() {
        // given
        val socialId = "social-id"

        registrationRepository.save(RegistrationCreator.create(socialId, SOCIAL_TYPE))

        // when
        Assertions.assertThatThrownBy {
            authService.login(LoginRequest(token = "token", socialType = SOCIAL_TYPE))
        }.isInstanceOf(ForbiddenException::class.java)
    }

    private class StubNaverAuthApiClient : NaverAuthApiClient {
        override fun getProfileInfo(accessToken: String?): NaverProfileResponse {
            return NaverProfileResponse(
                NaverProfileInfoResponse(id = SOCIAL_ID)
            )
        }
    }

}
