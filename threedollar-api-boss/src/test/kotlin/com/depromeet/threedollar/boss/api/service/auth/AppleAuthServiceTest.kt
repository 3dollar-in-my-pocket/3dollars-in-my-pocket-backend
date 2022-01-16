package com.depromeet.threedollar.boss.api.service.auth

import com.depromeet.threedollar.boss.api.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.document.boss.document.account.BossAccount
import com.depromeet.threedollar.document.boss.document.account.BossAccountRepository
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialInfo
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import com.depromeet.threedollar.external.client.apple.AppleTokenProvider
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class AppleAuthServiceTest(
    private val bossAccountRepository: BossAccountRepository
) {

    private lateinit var authService: AuthService

    @BeforeEach
    fun setUp() {
        authService = AppleAuthService(bossAccountRepository, StubAppleTokenProvider())
    }

    @AfterEach
    fun cleanUp() {
        bossAccountRepository.deleteAll()
    }

    @Test
    fun `애플 로그인이 성공하면 ID가 반환된다`() {
        // given
        val bossAccount = BossAccount(
            name = "사장님",
            socialInfo = BossAccountSocialInfo(SOCIAL_ID, SOCIAL_TYPE),
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
        Assertions.assertThatThrownBy { authService.login(LoginRequest("token", SOCIAL_TYPE)) }.isInstanceOf(NotFoundException::class.java)
    }

    private class StubAppleTokenProvider : AppleTokenProvider {
        override fun getSocialIdFromIdToken(idToken: String): String {
            return SOCIAL_ID
        }
    }

    companion object {
        private const val SOCIAL_ID = "social-id"
        private val SOCIAL_TYPE = BossAccountSocialType.APPLE
    }

}