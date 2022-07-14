package com.depromeet.threedollar.api.bossservice.service.auth

import com.depromeet.threedollar.api.bossservice.IntegrationTest
import com.depromeet.threedollar.api.bossservice.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.RegistrationFixture
import com.depromeet.threedollar.infrastructure.external.client.google.GoogleAuthApiClient
import com.depromeet.threedollar.infrastructure.external.client.google.dto.response.GoogleProfileInfoResponse
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private const val SOCIAL_ID = "social-id"
private val SOCIAL_TYPE = BossAccountSocialType.GOOGLE

internal class GoogleAuthServiceTest(
    private val bossAccountRepository: BossAccountRepository,
    private val bossRegistrationRepository: BossRegistrationRepository,
) : IntegrationTest() {

    private lateinit var authService: AuthService

    @BeforeEach
    fun setUp() {
        authService = GoogleAuthService(bossAccountRepository, bossRegistrationRepository, StubGoogleAuthApiClient())
    }

    @AfterEach
    fun cleanUp() {
        bossRegistrationRepository.deleteAll()
        bossAccountRepository.deleteAll()
    }

    @Test
    fun `구글 로그인이 성공하면 사장님 계정의 ID가 반환된다`() {
        // given
        val bossAccount = BossAccountFixture.create(
            name = "사장님",
            socialId = SOCIAL_ID,
            socialType = SOCIAL_TYPE
        )
        bossAccountRepository.save(bossAccount)

        // when
        val bossId = authService.login(LoginRequest("token", SOCIAL_TYPE))

        // then
        assertThat(bossId).isEqualTo(bossAccount.id)
    }

    @Test
    fun `구글 로그인시 가입한 유저가 아니면 404 에러 발생`() {
        // when & then
        assertThatThrownBy {
            authService.login(LoginRequest("token", SOCIAL_TYPE))
        }.isInstanceOf(NotFoundException::class.java)
    }

    @Test
    fun `구글 로그인시 가입 승인 대기중인 유저면 Registration Id를 반환한다`() {
        // given
        val registration = RegistrationFixture.create(SOCIAL_ID, SOCIAL_TYPE)
        bossRegistrationRepository.save(registration)

        // when
        val bossId = authService.login(LoginRequest(token = "token", socialType = SOCIAL_TYPE))

        // then
        assertThat(bossId).isEqualTo(registration.id)
    }

    private class StubGoogleAuthApiClient :
        GoogleAuthApiClient {
        override fun getProfileInfo(accessToken: String?): GoogleProfileInfoResponse {
            return GoogleProfileInfoResponse.testInstance(SOCIAL_ID)
        }
    }

}
