package com.depromeet.threedollar.api.admin.controller.auth

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post
import com.depromeet.threedollar.api.admin.controller.SetupAdminControllerTest
import com.depromeet.threedollar.api.admin.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.domain.rds.user.domain.admin.AdminCreator
import com.depromeet.threedollar.external.client.google.GoogleAuthApiClient
import com.depromeet.threedollar.external.client.google.dto.response.GoogleProfileInfoResponse

internal class AuthControllerTest : SetupAdminControllerTest() {

    @MockBean
    private lateinit var googleAuthApiClient: GoogleAuthApiClient

    @AfterEach
    fun cleanUp() {
        super.cleanup()
    }

    @DisplayName("POST /api/v2/login")
    @Nested
    inner class LoginApiTest {

        @Test
        fun 구글_로그인_요청이_성공하면_토큰이_반환된다() {
            // given
            val email = "will.seungho@gmail.com"
            val admin = AdminCreator.create(
                email = email,
                name = "관리자 계정",
            )
            adminRepository.save(admin)

            `when`(googleAuthApiClient.getProfileInfo(any())).thenReturn(GoogleProfileInfoResponse.testInstance("google-social-id", email, "구글 계정 이름"))

            val request = LoginRequest(token = "token")

            // when & then
            mockMvc.post("/v1/auth/login") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
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
