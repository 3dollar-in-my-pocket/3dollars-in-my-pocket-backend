package com.depromeet.threedollar.api.bossservice.controller.auth

import com.depromeet.threedollar.api.bossservice.SetupBossAccountControllerTest
import com.depromeet.threedollar.api.bossservice.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.api.bossservice.service.auth.dto.request.SignupRequest
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.RegistrationFixture
import com.depromeet.threedollar.infrastructure.external.client.apple.AppleTokenDecoder
import com.depromeet.threedollar.infrastructure.external.client.google.GoogleAuthApiClient
import com.depromeet.threedollar.infrastructure.external.client.google.dto.response.GoogleProfileInfoResponse
import com.depromeet.threedollar.infrastructure.external.client.kakao.KaKaoAuthApiClient
import com.depromeet.threedollar.infrastructure.external.client.kakao.dto.response.KaKaoProfileResponse
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post

internal class AuthControllerTest(
    private val bossRegistrationRepository: BossRegistrationRepository,
) : SetupBossAccountControllerTest() {

    @MockkBean
    private lateinit var kaKaoAuthApiClient: KaKaoAuthApiClient

    @MockkBean
    private lateinit var googleAuthApiClient: GoogleAuthApiClient

    @MockkBean
    private lateinit var appleTokenDecoder: AppleTokenDecoder

    @BeforeEach
    fun mockAuthApiClient() {
        every { kaKaoAuthApiClient.getProfileInfo(any()) } returns KaKaoProfileResponse.testInstance(KAKAO_SOCIAL_ID)
        every { appleTokenDecoder.getSocialIdFromIdToken(any()) } returns APPLE_SOCIAL_ID
        every { googleAuthApiClient.getProfileInfo(any()) } returns GoogleProfileInfoResponse.testInstance(GOOGLE_SOCIAL_ID)
    }

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        bossRegistrationRepository.deleteAll()
    }

    @Nested
    inner class KaKaoApiTest {

        @Test
        fun 카카오_회원가입_요청이_성공하면_토큰이_반환된다() {
            // given
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
        fun 카카오_로그인_요청이_성공하면_토큰이_반환된다() {
            // given
            val boss = BossAccountFixture.create(KAKAO_SOCIAL_ID, BossAccountSocialType.KAKAO, "카카오 계정")
            bossAccountRepository.save(boss)

            val request = LoginRequest(token = "token", socialType = BossAccountSocialType.KAKAO)

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
                    jsonPath("$.data.bossId") { value(boss.id) }
                    jsonPath("$.data.token") { isNotEmpty() }
                }
        }

        @Test
        fun 카카오_로그인시_가입승인_대기중인경우_토큰이_반환된다() {
            // given
            val registration = RegistrationFixture.create(KAKAO_SOCIAL_ID, BossAccountSocialType.KAKAO)
            bossRegistrationRepository.save(registration)

            val request = LoginRequest(token = "token", socialType = BossAccountSocialType.KAKAO)

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
                    jsonPath("$.data.bossId") { value(registration.id) }
                    jsonPath("$.data.token") { isNotEmpty() }
                }
        }

    }

    @Nested
    inner class AppleAuthApiTest {

        @Test
        fun 애플_회원가입_요청이_성공하면_토큰이_반환된다() {
            // given
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
        fun 애플_로그인_요청이_성공하면_토큰이_반환된다() {
            // given
            val boss = BossAccountFixture.create(APPLE_SOCIAL_ID, BossAccountSocialType.APPLE, "애플 계정")
            bossAccountRepository.save(boss)

            val request = LoginRequest(token = "token", socialType = BossAccountSocialType.APPLE)

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
                    jsonPath("$.data.bossId") { value(boss.id) }
                    jsonPath("$.data.token") { isNotEmpty() }
                }
        }

        @Test
        fun 애플_로그인시_가입승인_대기중인경우_토큰이_반환된다() {
            // given
            val registration = RegistrationFixture.create(APPLE_SOCIAL_ID, BossAccountSocialType.APPLE)
            bossRegistrationRepository.save(registration)

            val request = LoginRequest(token = "token", socialType = BossAccountSocialType.APPLE)

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
                    jsonPath("$.data.bossId") { value(registration.id) }
                    jsonPath("$.data.token") { isNotEmpty() }
                }
        }

    }

    @Nested
    inner class GoogleAuthApiTest {

        @Test
        fun 구글_회원가입_요청이_성공하면_토큰이_반환된다() {
            // given
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
        fun 구글_로그인_요청이_성공하면_토큰이_반환된다() {
            // given
            val boss = BossAccountFixture.create(GOOGLE_SOCIAL_ID, BossAccountSocialType.GOOGLE, "구글 계정")
            bossAccountRepository.save(boss)

            val request = LoginRequest(token = "token", socialType = BossAccountSocialType.GOOGLE)

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
                    jsonPath("$.data.bossId") { value(boss.id) }
                    jsonPath("$.data.token") { isNotEmpty() }
                }
        }

        @Test
        fun 구글_로그인시_가입승인_대기중인경우_토큰이_반환된다() {
            // given
            val registration = RegistrationFixture.create(GOOGLE_SOCIAL_ID, BossAccountSocialType.GOOGLE)
            bossRegistrationRepository.save(registration)

            val request = LoginRequest(token = "token", socialType = BossAccountSocialType.GOOGLE)

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
                    jsonPath("$.data.bossId") { value(registration.id) }
                    jsonPath("$.data.token") { isNotEmpty() }
                }
        }

    }

    @Nested
    inner class NaverAuthApiTest {

        @Test
        fun 네이버_회원가입_요청시_ServiceUnavailable_에러가_발생한다() {
            // given
            val request = SignupRequest(
                token = "token",
                socialType = BossAccountSocialType.NAVER,
                bossName = "bossName",
                businessNumber = "000-00-00000",
                storeName = "가게 이름",
                storeCategoriesIds = setOf(),
                contactsNumber = "010-1234-1234",
                certificationPhotoUrl = "https://photo.png"
            )

            // when & then
            mockMvc.post("/v1/auth/signup") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }
                .andDo {
                    print()
                }
                .andExpect {
                    status { isServiceUnavailable() }
                    jsonPath("$.resultCode") { value(ErrorCode.E503_SERVICE_UNAVAILABLE.code) }
                    jsonPath("$.message") { value(ErrorCode.E503_SERVICE_UNAVAILABLE.message) }
                }
        }

        @Test
        fun 네이버_로그인_요청시_ServiceUnavailable_에러가_발생한다() {
            // given
            val request = LoginRequest(token = "token", socialType = BossAccountSocialType.NAVER)

            // when & then
            mockMvc.post("/v1/auth/login") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }
                .andDo {
                    print()
                }
                .andExpect {
                    status { isServiceUnavailable() }
                    jsonPath("$.resultCode") { value(ErrorCode.E503_SERVICE_UNAVAILABLE.code) }
                    jsonPath("$.message") { value(ErrorCode.E503_SERVICE_UNAVAILABLE.message) }
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

        @Test
        fun 인증_헤더가_없는경우_로그아웃_성공시_401() {
            // when & then
            mockMvc.post("/v1/auth/logout") {
            }
                .andDo {
                    print()
                }
                .andExpect {
                    status { isUnauthorized() }
                    jsonPath("$.resultCode") { value(ErrorCode.E401_UNAUTHORIZED.code) }
                    jsonPath("$.message") { value(ErrorCode.E401_UNAUTHORIZED.message) }
                }
        }

    }

    @DisplayName("DELETE /api/v2/signout")
    @Nested
    inner class SignOutApiTest {

        @Test
        fun 회원탈퇴_요청이_성공하면_200_OK() {
            // when & then
            mockMvc.delete("/v1/auth/signout") {
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

    companion object {
        private const val KAKAO_SOCIAL_ID = "kakao-social-id"
        private const val GOOGLE_SOCIAL_ID = "google-social-id"
        private const val APPLE_SOCIAL_ID = "apple-social-id"
    }

}
