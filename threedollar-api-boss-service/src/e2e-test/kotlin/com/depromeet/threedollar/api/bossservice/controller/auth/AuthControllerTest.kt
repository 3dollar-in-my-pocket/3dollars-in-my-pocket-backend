package com.depromeet.threedollar.api.bossservice.controller.auth

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post
import com.depromeet.threedollar.api.bossservice.SetupBossAccountControllerTest
import com.depromeet.threedollar.api.bossservice.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.api.bossservice.service.auth.dto.request.SignupRequest
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountCreator
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.RegistrationCreator
import com.depromeet.threedollar.external.client.apple.AppleTokenDecoder
import com.depromeet.threedollar.external.client.google.GoogleAuthApiClient
import com.depromeet.threedollar.external.client.google.dto.response.GoogleProfileInfoResponse
import com.depromeet.threedollar.external.client.kakao.KaKaoAuthApiClient
import com.depromeet.threedollar.external.client.kakao.dto.response.KaKaoProfileResponse
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every

internal class AuthControllerTest(
    private val bossRegistrationRepository: BossRegistrationRepository,
) : SetupBossAccountControllerTest() {

    @MockkBean
    private lateinit var kaKaoAuthApiClient: KaKaoAuthApiClient

    @MockkBean
    private lateinit var googleAuthApiClient: GoogleAuthApiClient

    @MockkBean
    private lateinit var appleTokenDecoder: AppleTokenDecoder

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        bossRegistrationRepository.deleteAll()
    }

    @DisplayName("POST /api/v2/signup")
    @Nested
    inner class SignupApiTest {

        @Test
        fun 카카오_회원가입_요청이_성공하면_토큰이_반환된다() {
            // given
            every { kaKaoAuthApiClient.getProfileInfo(any()) } returns KaKaoProfileResponse.testInstance("kakao-socialId")

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
        fun 카카오_회원가입시_이미_존재하는_사장님_계정인경우_409_에러가_발생한다() {
            // given
            val boss = BossAccountCreator.create("google-social-id", BossAccountSocialType.KAKAO, "카카오 계정")
            bossAccountRepository.save(boss)

            every { kaKaoAuthApiClient.getProfileInfo(any()) } returns KaKaoProfileResponse.testInstance(boss.socialInfo.socialId)

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
                    status { isConflict() }
                    jsonPath("$.resultCode") { ErrorCode.CONFLICT_BOSS_ACCOUNT.code }
                    jsonPath("$.resultCode") { ErrorCode.CONFLICT_BOSS_ACCOUNT.message }
                }
        }

        @Test
        fun 카카오_회원가입시_가입_승인_대기중인경우_403_에러가_발생한다() {
            // given
            val registration = RegistrationCreator.create("google-social-id", BossAccountSocialType.KAKAO)
            bossRegistrationRepository.save(registration)

            every { kaKaoAuthApiClient.getProfileInfo(any()) } returns KaKaoProfileResponse.testInstance(registration.boss.socialInfo.socialId)

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
                    status { isForbidden() }
                    jsonPath("$.resultCode") { ErrorCode.FORBIDDEN_WAITING_APPROVE_BOSS_ACCOUNT.code }
                    jsonPath("$.resultCode") { ErrorCode.FORBIDDEN_WAITING_APPROVE_BOSS_ACCOUNT.message }
                }
        }

        @Test
        fun 애플_회원가입_요청이_성공하면_토큰이_반환된다() {
            // given
            every { appleTokenDecoder.getSocialIdFromIdToken(any()) } returns "apple-social-id"

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
        fun 애플_회원가입시_이미_존재하는_사장님_계정인경우_409_에러가_발생한다() {
            // given
            val boss = BossAccountCreator.create("apple-social-id", BossAccountSocialType.APPLE, "애플 계정")
            bossAccountRepository.save(boss)

            every { appleTokenDecoder.getSocialIdFromIdToken(any()) } returns boss.socialInfo.socialId

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
                    status { isConflict() }
                    jsonPath("$.resultCode") { ErrorCode.CONFLICT_BOSS_ACCOUNT.code }
                    jsonPath("$.resultCode") { ErrorCode.CONFLICT_BOSS_ACCOUNT.message }
                }
        }

        @Test
        fun 애플_회원가입시_가입_승인_대기중인경우_403_에러가_발생한다() {
            // given
            val registration = RegistrationCreator.create("apple-social-id", BossAccountSocialType.APPLE)
            bossRegistrationRepository.save(registration)

            every { appleTokenDecoder.getSocialIdFromIdToken(any()) } returns registration.boss.socialInfo.socialId

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
                    status { isForbidden() }
                    jsonPath("$.resultCode") { ErrorCode.FORBIDDEN_WAITING_APPROVE_BOSS_ACCOUNT.code }
                    jsonPath("$.resultCode") { ErrorCode.FORBIDDEN_WAITING_APPROVE_BOSS_ACCOUNT.message }
                }
        }

        @Test
        fun 구글_회원가입_요청이_성공하면_토큰이_반환된다() {
            // given
            every { googleAuthApiClient.getProfileInfo(any()) } returns GoogleProfileInfoResponse.testInstance("google-social-id")

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
        fun 구글_회원가입시_이미_존재하는_사장님_계정인경우_409_에러가_발생한다() {
            // given
            val boss = BossAccountCreator.create("google-social-id", BossAccountSocialType.GOOGLE, "구글 계정")
            bossAccountRepository.save(boss)

            every { googleAuthApiClient.getProfileInfo(any()) } returns GoogleProfileInfoResponse.testInstance(boss.socialInfo.socialId)

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
                    status { isConflict() }
                    jsonPath("$.resultCode") { ErrorCode.CONFLICT_BOSS_ACCOUNT.code }
                    jsonPath("$.resultCode") { ErrorCode.CONFLICT_BOSS_ACCOUNT.message }
                }
        }

        @Test
        fun 구글_회원가입시_가입_승인_대기중인경우_403_에러가_발생한다() {
            // given
            val registration = RegistrationCreator.create("google-social-id", BossAccountSocialType.GOOGLE)
            bossRegistrationRepository.save(registration)

            every { googleAuthApiClient.getProfileInfo(any()) } returns GoogleProfileInfoResponse.testInstance(registration.boss.socialInfo.socialId)

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
                    status { isForbidden() }
                    jsonPath("$.resultCode") { ErrorCode.FORBIDDEN_WAITING_APPROVE_BOSS_ACCOUNT.code }
                    jsonPath("$.resultCode") { ErrorCode.FORBIDDEN_WAITING_APPROVE_BOSS_ACCOUNT.message }
                }
        }

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
                    jsonPath("$.resultCode") { value(ErrorCode.SERVICE_UNAVAILABLE.code) }
                    jsonPath("$.message") { value(ErrorCode.SERVICE_UNAVAILABLE.message) }
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

            every { kaKaoAuthApiClient.getProfileInfo(any()) } returns KaKaoProfileResponse.testInstance(boss.socialInfo.socialId)

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
            val registration = RegistrationCreator.create("google-social-id", BossAccountSocialType.KAKAO)
            bossRegistrationRepository.save(registration)

            every { kaKaoAuthApiClient.getProfileInfo(any()) } returns KaKaoProfileResponse.testInstance(registration.boss.socialInfo.socialId)

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

        @Test
        fun 애플_로그인_요청이_성공하면_토큰이_반환된다() {
            // given
            val boss = BossAccountCreator.create("google-social-id", BossAccountSocialType.APPLE, "애플 계정")
            bossAccountRepository.save(boss)

            every { appleTokenDecoder.getSocialIdFromIdToken(any()) } returns boss.socialInfo.socialId

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
            val registration = RegistrationCreator.create("google-social-id", BossAccountSocialType.APPLE)
            bossRegistrationRepository.save(registration)

            every { appleTokenDecoder.getSocialIdFromIdToken(any()) } returns registration.boss.socialInfo.socialId

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

        @Test
        fun 구글_로그인_요청이_성공하면_토큰이_반환된다() {
            // given
            val boss = BossAccountCreator.create("google-social-id", BossAccountSocialType.GOOGLE, "구글 계정")
            bossAccountRepository.save(boss)

            every { googleAuthApiClient.getProfileInfo(any()) } returns GoogleProfileInfoResponse.testInstance(boss.socialInfo.socialId)

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
            val registration = RegistrationCreator.create("google-social-id", BossAccountSocialType.GOOGLE)
            bossRegistrationRepository.save(registration)

            every { googleAuthApiClient.getProfileInfo(any()) } returns GoogleProfileInfoResponse.testInstance(registration.boss.socialInfo.socialId)

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
                    jsonPath("$.resultCode") { value(ErrorCode.SERVICE_UNAVAILABLE.code) }
                    jsonPath("$.message") { value(ErrorCode.SERVICE_UNAVAILABLE.message) }
                }
        }

    }

    @DisplayName("POST /boss/v1/auth/logout")
    @Nested
    inner class LogoutApiTest {

        @Test
        fun 로그아웃_성공시_200_OK() {
            // when & then
            mockMvc.post("/v1/auth/logout")
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

}
