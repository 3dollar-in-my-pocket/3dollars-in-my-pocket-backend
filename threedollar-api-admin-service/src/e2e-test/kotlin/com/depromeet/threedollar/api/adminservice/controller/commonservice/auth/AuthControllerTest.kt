package com.depromeet.threedollar.api.adminservice.controller.commonservice.auth

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post
import com.depromeet.threedollar.api.adminservice.SetupAdminControllerTest
import com.depromeet.threedollar.api.adminservice.service.commonservice.auth.dto.request.LoginRequest
import com.depromeet.threedollar.domain.rds.domain.commonservice.admin.AdminFixture
import com.depromeet.threedollar.external.client.google.GoogleAuthApiClient
import com.depromeet.threedollar.external.client.google.dto.response.GoogleProfileInfoResponse
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every

internal class AuthControllerTest : SetupAdminControllerTest() {

    @MockkBean
    private lateinit var googleAuthApiClient: GoogleAuthApiClient

    @DisplayName("POST /api/v2/login")
    @Nested
    inner class LoginApiTest {

        @Test
        fun 구글_로그인_요청이_성공하면_토큰이_반환된다() {
            // given
            val email = "will.seungho@gmail.com"
            val admin = AdminFixture.create(email, "관리자")
            adminRepository.save(admin)

            every { googleAuthApiClient.getProfileInfo(any()) } returns GoogleProfileInfoResponse.testInstance("google-social-id", email, "구글 계정 이름")

            val request = LoginRequest(token = "token")

            // when & then
            mockMvc.post("/v1/auth/login") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }
                .andDo {
                    print()
                }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.data.token") { isNotEmpty() }
                }
        }

    }

    @DisplayName("POST /boss/v1/auth/logout")
    @Nested
    inner class LogoutApiTest {

        @Test
        fun 로그아웃_성공시_200_OK() {
            // when & then
            mockMvc.post("/v1/auth/logout") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }
                .andDo {
                    print()
                }
                .andExpect {
                    status { isOk() }
                }
        }

    }

}
