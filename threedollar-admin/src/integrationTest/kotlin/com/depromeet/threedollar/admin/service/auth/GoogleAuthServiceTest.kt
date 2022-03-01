package com.depromeet.threedollar.admin.service.auth

import com.depromeet.threedollar.admin.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import com.depromeet.threedollar.domain.user.domain.admin.AdminCreator
import com.depromeet.threedollar.domain.user.domain.admin.AdminRepository
import com.depromeet.threedollar.external.client.google.GoogleAuthApiClient
import com.depromeet.threedollar.external.client.google.dto.response.GoogleProfileInfoResponse
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class GoogleAuthServiceTest(
    private val adminRepository: AdminRepository
) {

    private lateinit var authService: AuthService

    @BeforeEach
    fun setUp() {
        authService = GoogleAuthService(adminRepository, StubGoogleAuthApiClient())
    }

    @AfterEach
    fun cleanUp() {
        adminRepository.deleteAll()
    }

    @Test
    fun `구글 로그인이 성공하면 관리자 계정의 ID가 반환된다`() {
        // given
        val admin = AdminCreator.create(
            EMAIL,
            "강승호"
        )
        adminRepository.save(admin)

        // when
        val adminId = authService.login(LoginRequest("token", SOCIAL_TYPE))

        // then
        Assertions.assertThat(adminId).isEqualTo(admin.id)
    }

    @Test
    fun `구글 로그인시 가입한 관리자가 아니면 404 에러 발생`() {
        // when & then
        Assertions.assertThatThrownBy {
            authService.login(LoginRequest("token", SOCIAL_TYPE))
        }.isInstanceOf(NotFoundException::class.java)
    }

    private class StubGoogleAuthApiClient : GoogleAuthApiClient {
        override fun getProfileInfo(accessToken: String?): GoogleProfileInfoResponse {
            return GoogleProfileInfoResponse.testInstance("id", EMAIL, "강승호")
        }
    }

    companion object {
        private const val EMAIL = "will.seunho@gmail.com"
        private val SOCIAL_TYPE = BossAccountSocialType.GOOGLE
    }

}
