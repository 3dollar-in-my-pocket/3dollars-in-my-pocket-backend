package com.depromeet.threedollar.api.boss.controller.auth

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post
import com.depromeet.threedollar.api.boss.controller.SetupBossAccountControllerTest
import com.depromeet.threedollar.api.boss.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.api.boss.service.auth.dto.request.SignupRequest
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.external.client.apple.AppleTokenDecoder
import com.depromeet.threedollar.external.client.google.GoogleAuthApiClient
import com.depromeet.threedollar.external.client.google.dto.response.GoogleProfileInfoResponse
import com.depromeet.threedollar.external.client.kakao.KaKaoAuthApiClient
import com.depromeet.threedollar.external.client.kakao.dto.response.KaKaoProfileResponse

internal class AuthControllerTest : SetupBossAccountControllerTest() {

    @MockBean
    private lateinit var kaKaoAuthApiClient: KaKaoAuthApiClient

    @MockBean
    private lateinit var googleAuthApiClient: GoogleAuthApiClient

    @MockBean
    private lateinit var appleTokenDecoder: AppleTokenDecoder

    @AfterEach
    fun cleanUp() {
        super.cleanup()
    }

    @DisplayName("POST /api/v2/signup")
    @Nested
    inner class SignupApiTest {

        @Test
        fun 카카오_회원가입_요청이_성공하면_토큰이_반환된다() {
            // given
            `when`(kaKaoAuthApiClient.getProfileInfo(any())).thenReturn(KaKaoProfileResponse.testInstance("kakao-socialId"))

            val request = SignupRequest(
                token = "token",
                socialType = BossAccountSocialType.KAKAO,
                bossName = "bossName",
                businessNumber = "000-00-00000",
                storeName = "가게 이름",
                storeCategoriesIds = setOf(),
                contactsNumber = "010-1234-1234",
                certificationPhotoUrl = "https://photo.png"
            )

            // when & then
            mockMvc.post("/v1/auth/signup") {
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

        @Test
        fun 애플_회원가입_요청이_성공하면_토큰이_반환된다() {
            // given
            `when`(appleTokenDecoder.getSocialIdFromIdToken(any())).thenReturn("apple-social-id")

            val request = SignupRequest(
                token = "token",
                socialType = BossAccountSocialType.APPLE,
                bossName = "bossName",
                businessNumber = "000-00-00000",
                storeName = "가게 이름",
                storeCategoriesIds = setOf(),
                contactsNumber = "010-1234-1234",
                certificationPhotoUrl = "https://photo.png"
            )

            // when & then
            mockMvc.post("/v1/auth/signup") {
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

        @Test
        fun 구글_회원가입_요청이_성공하면_토큰이_반환된다() {
            // given
            `when`(googleAuthApiClient.getProfileInfo(any())).thenReturn(GoogleProfileInfoResponse.testInstance("google-social-id"))

            val request = SignupRequest(
                token = "token",
                socialType = BossAccountSocialType.GOOGLE,
                bossName = "bossName",
                businessNumber = "000-00-00000",
                storeName = "가게 이름",
                storeCategoriesIds = setOf(),
                contactsNumber = "010-1234-1234",
                certificationPhotoUrl = "https://photo.png"
            )

            // when & then
            mockMvc.post("/v1/auth/signup") {
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

    @DisplayName("POST /api/v2/login")
    @Nested
    inner class LoginApiTest {

        @Test
        fun 카카오_로그인_요청이_성공하면_토큰이_반환된다() {
            // given
            val boss = BossAccountCreator.create("google-social-id", BossAccountSocialType.KAKAO, "카카오 계정")
            bossAccountRepository.save(boss)

            `when`(kaKaoAuthApiClient.getProfileInfo(any())).thenReturn(KaKaoProfileResponse.testInstance(boss.socialInfo.socialId))

            val request = LoginRequest(token = "token", socialType = BossAccountSocialType.KAKAO)

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
                    jsonPath("$.data.bossId") { value(boss.id) }
                    jsonPath("$.data.token") { isNotEmpty() }
                }
        }

        @Test
        fun 애플_로그인_요청이_성공하면_토큰이_반환된다() {
            // given
            val boss = BossAccountCreator.create("google-social-id", BossAccountSocialType.APPLE, "애플 계정")
            bossAccountRepository.save(boss)

            `when`(appleTokenDecoder.getSocialIdFromIdToken(any())).thenReturn(boss.socialInfo.socialId)

            val request = LoginRequest(token = "token", socialType = BossAccountSocialType.APPLE)

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
                    jsonPath("$.data.bossId") { value(boss.id) }
                    jsonPath("$.data.token") { isNotEmpty() }
                }
        }

        @Test
        fun 구글_로그인_요청이_성공하면_토큰이_반환된다() {
            // given
            val boss = BossAccountCreator.create("google-social-id", BossAccountSocialType.GOOGLE, "구글 계정")
            bossAccountRepository.save(boss)

            `when`(googleAuthApiClient.getProfileInfo(any())).thenReturn(GoogleProfileInfoResponse.testInstance(boss.socialInfo.socialId))

            val request = LoginRequest(token = "token", socialType = BossAccountSocialType.GOOGLE)

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
                    jsonPath("$.data.bossId") { value(boss.id) }
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

    @DisplayName("DELETE /api/v2/signout")
    @Nested
    inner class SignOutApiTest {

        @Test
        fun 회원탈퇴_요청이_성공하면_200_OK() {
            // when & then
            mockMvc.delete("/v1/auth/sign-out") {
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
