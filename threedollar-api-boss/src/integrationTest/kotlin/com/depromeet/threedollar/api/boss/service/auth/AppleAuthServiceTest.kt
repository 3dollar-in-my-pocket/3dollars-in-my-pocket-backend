package com.depromeet.threedollar.api.boss.service.auth

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import com.depromeet.threedollar.api.boss.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationRepository
import com.depromeet.threedollar.external.client.apple.AppleTokenDecoder

private const val SOCIAL_ID = "social-id"
private val SOCIAL_TYPE = BossAccountSocialType.APPLE

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class AppleAuthServiceTest(
    private val bossAccountRepository: BossAccountRepository,
    private val registrationRepository: RegistrationRepository
) {

    private lateinit var authService: AuthService

    @BeforeEach
    fun setUp() {
        authService = AppleAuthService(bossAccountRepository, registrationRepository, StubAppleTokenDecoder())
    }

    @AfterEach
    fun cleanUp() {
        registrationRepository.deleteAll()
        bossAccountRepository.deleteAll()
    }

    @Test
    fun `애플 로그인이 성공하면 사장님 계정의 ID가 반환된다`() {
        // given
        val bossAccount = BossAccountCreator.create(
            name = "사장님",
            socialId = SOCIAL_ID,
            socialType = SOCIAL_TYPE,
        )
        bossAccountRepository.save(bossAccount)

        // when
        val accountId = authService.login(LoginRequest("token", SOCIAL_TYPE))

        // then
        assertThat(accountId).isEqualTo(bossAccount.id)
    }

    @Test
    fun `애플 로그인시 가입한 유저가 아니면 404 에러 발생`() {
        // when & then
        assertThatThrownBy {
            authService.login(LoginRequest(token = "token", socialType = SOCIAL_TYPE))
        }.isInstanceOf(NotFoundException::class.java)
    }

    @Test
    fun `애플 로그인시 가입 승인 대기중인 유저면 Registration Id를 반환한다`() {
        // given
        val registration = RegistrationCreator.create(SOCIAL_ID, SOCIAL_TYPE)
        registrationRepository.save(registration)

        // when
        val bossId = authService.login(LoginRequest(token = "token", socialType = SOCIAL_TYPE))

        // then
        assertThat(bossId).isEqualTo(registration.id)
    }

    private class StubAppleTokenDecoder : AppleTokenDecoder {
        override fun getSocialIdFromIdToken(idToken: String): String {
            return SOCIAL_ID
        }
    }

}
