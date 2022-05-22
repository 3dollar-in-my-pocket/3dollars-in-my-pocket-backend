package com.depromeet.threedollar.api.user.controller.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.depromeet.threedollar.api.core.common.dto.ApiResponse;
import com.depromeet.threedollar.api.user.controller.SetupUserControllerTest;
import com.depromeet.threedollar.api.user.service.auth.dto.request.LoginRequest;
import com.depromeet.threedollar.api.user.service.auth.dto.request.SignUpRequest;
import com.depromeet.threedollar.api.user.service.auth.dto.response.LoginResponse;
import com.depromeet.threedollar.domain.rds.user.domain.user.User;
import com.depromeet.threedollar.domain.rds.user.domain.user.UserCreator;
import com.depromeet.threedollar.domain.rds.user.domain.user.UserSocialType;
import com.depromeet.threedollar.external.client.apple.AppleTokenDecoder;
import com.depromeet.threedollar.external.client.google.GoogleAuthApiClient;
import com.depromeet.threedollar.external.client.google.dto.response.GoogleProfileInfoResponse;
import com.depromeet.threedollar.external.client.kakao.KaKaoAuthApiClient;
import com.depromeet.threedollar.external.client.kakao.dto.response.KaKaoProfileResponse;

class AuthControllerTest extends SetupUserControllerTest {

    private AuthMockApiCaller authMockApiCaller;

    @MockBean
    private KaKaoAuthApiClient kaKaoAuthApiClient;

    @MockBean
    private GoogleAuthApiClient googleAuthApiClient;

    @MockBean
    private AppleTokenDecoder appleTokenDecoder;

    @BeforeEach
    void setUp() {
        authMockApiCaller = new AuthMockApiCaller(mockMvc, objectMapper);
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
    }

    @DisplayName("POST /api/v2/signup")
    @Nested
    class SignUpApiTest {

        @Test
        void 카카오_회원가입_요청이_성공하면_인증_토큰이_반환된다() throws Exception {
            // given
            when(kaKaoAuthApiClient.getProfileInfo(any())).thenReturn(KaKaoProfileResponse.testInstance("kakao-social-id"));

            SignUpRequest request = SignUpRequest.testBuilder()
                .token("social-access-token")
                .name("가슴속 삼천원")
                .socialType(UserSocialType.KAKAO)
                .build();

            // when
            ApiResponse<LoginResponse> response = authMockApiCaller.signUp(request, 200);

            // then
            assertAll(
                () -> assertThat(response.getResultCode()).isEmpty(),
                () -> assertThat(response.getMessage()).isEmpty(),
                () -> assertThat(response.getData().getToken()).isNotBlank()
            );
        }

        @Test
        void 애플_회원가입_요청이_성공하면_인증_토큰이_반환된다() throws Exception {
            // given
            when(appleTokenDecoder.getSocialIdFromIdToken(any())).thenReturn("apple-social-id");

            SignUpRequest request = SignUpRequest.testBuilder()
                .token("social-access-token")
                .name("가슴속 삼천원")
                .socialType(UserSocialType.APPLE)
                .build();

            // when
            ApiResponse<LoginResponse> response = authMockApiCaller.signUp(request, 200);

            // then
            assertAll(
                () -> assertThat(response.getResultCode()).isEmpty(),
                () -> assertThat(response.getMessage()).isEmpty(),
                () -> assertThat(response.getData().getToken()).isNotBlank()
            );
        }

        @Test
        void 구글_회원가입_요청이_성공하면_인증_토큰이_반환된다() throws Exception {
            // given
            when(googleAuthApiClient.getProfileInfo(any())).thenReturn(GoogleProfileInfoResponse.testInstance("google-social-id"));

            SignUpRequest request = SignUpRequest.testBuilder()
                .token("social-access-token")
                .name("가슴속 삼천원")
                .socialType(UserSocialType.GOOGLE)
                .build();

            // when
            ApiResponse<LoginResponse> response = authMockApiCaller.signUp(request, 200);

            // then
            assertAll(
                () -> assertThat(response.getResultCode()).isEmpty(),
                () -> assertThat(response.getMessage()).isEmpty(),
                () -> assertThat(response.getData().getToken()).isNotBlank()
            );
        }

    }

    @DisplayName("POST /api/v2/login")
    @Nested
    class LoginApiTest {

        @Test
        void 카카오_로그인_요청이_성공하면_인증_토큰이_반환된다() throws Exception {
            // given
            User user = UserCreator.create("kakao-social-id", UserSocialType.KAKAO, "카카오 계정");
            userRepository.save(user);

            when(kaKaoAuthApiClient.getProfileInfo(any())).thenReturn(KaKaoProfileResponse.testInstance(user.getSocialId()));

            LoginRequest request = LoginRequest.testBuilder()
                .token("kakao-access-token")
                .socialType(UserSocialType.KAKAO)
                .build();

            // when
            ApiResponse<LoginResponse> response = authMockApiCaller.login(request, 200);

            // then
            assertAll(
                () -> assertThat(response.getResultCode()).isEmpty(),
                () -> assertThat(response.getMessage()).isEmpty(),
                () -> assertThat(response.getData().getToken()).isNotBlank()
            );
        }

        @Test
        void 애플_로그인_요청이_성공하면_인증_토큰이_반환된다() throws Exception {
            // given
            User user = UserCreator.create("apple-social-id", UserSocialType.APPLE, "애플 계정");
            userRepository.save(user);

            when(appleTokenDecoder.getSocialIdFromIdToken(any())).thenReturn(user.getSocialId());

            LoginRequest request = LoginRequest.testBuilder()
                .token("apple-access-token")
                .socialType(UserSocialType.APPLE)
                .build();

            // when
            ApiResponse<LoginResponse> response = authMockApiCaller.login(request, 200);

            // then
            assertAll(
                () -> assertThat(response.getResultCode()).isEmpty(),
                () -> assertThat(response.getMessage()).isEmpty(),
                () -> assertThat(response.getData().getToken()).isNotBlank()
            );
        }

        @Test
        void 구글_로그인_요청이_성공하면_인증_토큰이_반환된다() throws Exception {
            // given
            User user = UserCreator.create("google-social-id", UserSocialType.GOOGLE, "구글 계정");
            userRepository.save(user);

            when(googleAuthApiClient.getProfileInfo(any())).thenReturn(GoogleProfileInfoResponse.testInstance(user.getSocialId()));

            LoginRequest request = LoginRequest.testBuilder()
                .token("google-access-token")
                .socialType(UserSocialType.GOOGLE)
                .build();

            // when
            ApiResponse<LoginResponse> response = authMockApiCaller.login(request, 200);

            // then
            assertAll(
                () -> assertThat(response.getResultCode()).isEmpty(),
                () -> assertThat(response.getMessage()).isEmpty(),
                () -> assertThat(response.getData().getToken()).isNotBlank()
            );
        }

    }

    @DisplayName("DELETE /api/v2/signout")
    @Nested
    class SignOutApiTest {

        @Test
        void 회원탈퇴_요청이_성공하면_200_OK() throws Exception {
            // when
            ApiResponse<String> response = authMockApiCaller.signOut(token, 200);

            // then
            assertAll(
                () -> assertThat(response.getResultCode()).isEmpty(),
                () -> assertThat(response.getMessage()).isEmpty(),
                () -> assertThat(response.getData()).isEqualTo(ApiResponse.OK.getData())
            );
        }

    }

    @DisplayName("DELETE /api/v2/logout")
    @Nested
    class LogoutApiTest {

        @Test
        void 로그아웃_성공시_200_OK() throws Exception {
            // when
            ApiResponse<String> response = authMockApiCaller.logout(token, 200);

            // then
            assertAll(
                () -> assertThat(response.getResultCode()).isEmpty(),
                () -> assertThat(response.getMessage()).isEmpty(),
                () -> assertThat(response.getData()).isEqualTo(ApiResponse.OK.getData())
            );
        }

    }

}
