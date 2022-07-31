package com.depromeet.threedollar.api.adminservice.service.commonservice.auth

import com.depromeet.threedollar.api.adminservice.IntegrationTest
import com.depromeet.threedollar.api.adminservice.service.commonservice.auth.dto.request.LoginRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.rds.domain.commonservice.admin.AdminFixture
import com.depromeet.threedollar.domain.rds.domain.commonservice.admin.AdminRepository
import com.depromeet.threedollar.infrastructure.external.client.google.GoogleAuthApiClient
import com.depromeet.threedollar.infrastructure.external.client.google.dto.response.GoogleProfileInfoResponse
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

private const val EMAIL = "will.seunho@gmail.com"

internal class GoogleAuthServiceTest(
    private val adminRepository: AdminRepository,
) : IntegrationTest() {

    private val authService: AuthService = GoogleAuthService(
        adminRepository = adminRepository,
        googleAuthApiClient = StubGoogleAuthApiClient()
    )

    @Nested
    inner class GoogleLoginTest {

        @Test
        fun `구글 로그인이 성공하면 관리자 계정의 ID가 반환된다`() {
            // given
            val admin = AdminFixture.create(
                email = EMAIL,
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

    private class StubGoogleAuthApiClient :
        GoogleAuthApiClient {

        override fun getProfileInfo(accessToken: String): GoogleProfileInfoResponse {
            return GoogleProfileInfoResponse.testInstance("id", EMAIL, "강승호")

        }
    }

}
