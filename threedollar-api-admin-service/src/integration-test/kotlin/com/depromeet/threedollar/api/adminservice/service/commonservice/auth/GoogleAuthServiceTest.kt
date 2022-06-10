package com.depromeet.threedollar.api.adminservice.service.commonservice.auth

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import com.depromeet.threedollar.api.adminservice.service.commonservice.auth.dto.request.LoginRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.rds.domain.commonservice.admin.AdminCreator
import com.depromeet.threedollar.domain.rds.domain.commonservice.admin.AdminRepository
import com.depromeet.threedollar.external.client.google.GoogleAuthApiClient
import com.depromeet.threedollar.external.client.google.dto.response.GoogleProfileInfoResponse

private const val EMAIL = "will.seunho@gmail.com"

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class GoogleAuthServiceTest(
    private val adminRepository: AdminRepository,
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

    @Nested
    inner class GoogleLoginTest {

        @Test
        fun `구글 로그인이 성공하면 관리자 계정의 ID가 반환된다`() {
            // given
            val admin = AdminCreator.create(
                email = EMAIL,
                name = "강승호"
            )
            adminRepository.save(admin)

            // when
            val adminId = authService.login(LoginRequest("token"))

            // then
            assertThat(adminId).isEqualTo(admin.id)
        }

        @Test
        fun `구글 로그인시 가입한 관리자가 아니면 404 에러 발생`() {
            // when & then
            assertThatThrownBy {
                authService.login(LoginRequest("token"))
            }.isInstanceOf(NotFoundException::class.java)
        }

    }

    private class StubGoogleAuthApiClient : GoogleAuthApiClient {

        override fun getProfileInfo(accessToken: String): GoogleProfileInfoResponse {
            return GoogleProfileInfoResponse.testInstance("id", EMAIL, "강승호")

        }
    }

}
