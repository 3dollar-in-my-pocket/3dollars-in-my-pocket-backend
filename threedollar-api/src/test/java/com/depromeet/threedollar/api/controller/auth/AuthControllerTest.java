package com.depromeet.threedollar.api.controller.auth;

import com.depromeet.threedollar.api.controller.SetupUserControllerTest;
import com.depromeet.threedollar.api.service.auth.dto.request.LoginRequest;
import com.depromeet.threedollar.api.service.auth.dto.request.SignUpRequest;
import com.depromeet.threedollar.api.service.auth.dto.response.LoginResponse;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserCreator;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import com.depromeet.threedollar.external.client.apple.AppleTokenProvider;
import com.depromeet.threedollar.external.client.google.GoogleApiClient;
import com.depromeet.threedollar.external.client.google.dto.response.GoogleProfileInfoResponse;
import com.depromeet.threedollar.external.client.kakao.KaKaoApiClient;
import com.depromeet.threedollar.external.client.kakao.dto.response.KaKaoProfileResponse;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthControllerTest extends SetupUserControllerTest {

    private AuthMockApiCaller authMockApiCaller;

    @MockBean
    private KaKaoApiClient kaKaoApiClient;

    @MockBean
    private GoogleApiClient googleApiClient;

    @MockBean
    private AppleTokenProvider appleTokenProvider;

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
    class 회원가입 {

        @Test
        void 카카오_회원가입_요청이_성공하면_토큰이_반환된다() throws Exception {
            // given
            when(kaKaoApiClient.getProfileInfo(any())).thenReturn(KaKaoProfileResponse.testInstance("social-id"));

            SignUpRequest request = SignUpRequest.testInstance("access-token", "will", UserSocialType.KAKAO);

            // when
            ApiResponse<LoginResponse> response = authMockApiCaller.signUp(request, 200);

            // then
            assertThat(response.getResultCode()).isEmpty();
            assertThat(response.getMessage()).isEmpty();
            assertThat(response.getData().getToken()).isNotBlank();
        }

        @Test
        void 애플_회원가입_요청이_성공하면_토큰이_반환된다() throws Exception {
            // given
            when(appleTokenProvider.getSocialIdFromIdToken(any())).thenReturn("social-id");

            SignUpRequest request = SignUpRequest.testInstance("access-token", "will", UserSocialType.APPLE);

            // when
            ApiResponse<LoginResponse> response = authMockApiCaller.signUp(request, 200);

            // then
            assertThat(response.getResultCode()).isEmpty();
            assertThat(response.getMessage()).isEmpty();
            assertThat(response.getData().getToken()).isNotBlank();
        }

        @Test
        void 구글_회원가입_요청이_성공하면_토큰이_반환된다() throws Exception {
            // given
            when(googleApiClient.getProfileInfo(any())).thenReturn(GoogleProfileInfoResponse.testInstance("social-id"));

            SignUpRequest request = SignUpRequest.testInstance("access-token", "will", UserSocialType.GOOGLE);

            // when
            ApiResponse<LoginResponse> response = authMockApiCaller.signUp(request, 200);

            // then
            assertThat(response.getResultCode()).isEmpty();
            assertThat(response.getMessage()).isEmpty();
            assertThat(response.getData().getToken()).isNotBlank();
        }

    }

    @DisplayName("POST /api/v2/login")
    @Nested
    class 로그인 {

        @Test
        void 카카오_로그인_요청이_성공하면_토큰이_반환된다() throws Exception {
            // given
            User user = UserCreator.create("social-id", UserSocialType.KAKAO, "카카오 계정");
            userRepository.save(user);

            when(kaKaoApiClient.getProfileInfo(any())).thenReturn(KaKaoProfileResponse.testInstance(user.getSocialId()));

            LoginRequest request = LoginRequest.testInstance("access-token", UserSocialType.KAKAO);

            // when
            ApiResponse<LoginResponse> response = authMockApiCaller.login(request, 200);

            // then
            assertThat(response.getResultCode()).isEmpty();
            assertThat(response.getMessage()).isEmpty();
            assertThat(response.getData().getToken()).isNotBlank();
        }

        @Test
        void 애플_로그인_요청이_성공하면_토큰이_반환된다() throws Exception {
            // given
            User user = UserCreator.create("social-id", UserSocialType.APPLE, "애플 계정");
            userRepository.save(user);

            when(appleTokenProvider.getSocialIdFromIdToken(any())).thenReturn(user.getSocialId());

            LoginRequest request = LoginRequest.testInstance("access-token", UserSocialType.APPLE);

            // when
            ApiResponse<LoginResponse> response = authMockApiCaller.login(request, 200);

            // then
            assertThat(response.getResultCode()).isEmpty();
            assertThat(response.getMessage()).isEmpty();
            assertThat(response.getData().getToken()).isNotBlank();
        }

        @Test
        void 구글_로그인_요청이_성공하면_토큰이_반환된다() throws Exception {
            // given
            User user = UserCreator.create("social-id", UserSocialType.GOOGLE, "구글 계정");
            userRepository.save(user);

            when(googleApiClient.getProfileInfo(any())).thenReturn(GoogleProfileInfoResponse.testInstance(user.getSocialId()));

            LoginRequest request = LoginRequest.testInstance("access-token", UserSocialType.GOOGLE);

            // when
            ApiResponse<LoginResponse> response = authMockApiCaller.login(request, 200);

            // then
            assertThat(response.getResultCode()).isEmpty();
            assertThat(response.getMessage()).isEmpty();
            assertThat(response.getData().getToken()).isNotBlank();
        }

    }

    @DisplayName("DELETE /api/v2/signout")
    @Nested
    class 회원탈퇴 {

        @Test
        void 회원탈퇴_요청이_성공하면_200_OK() throws Exception {
            // when
            ApiResponse<String> response = authMockApiCaller.signOut(token, 200);

            // then
            assertThat(response.getResultCode()).isEmpty();
            assertThat(response.getMessage()).isEmpty();
            assertThat(response.getData()).isEqualTo(ApiResponse.SUCCESS.getData());
        }

    }

    @DisplayName("DELETE /api/v2/logout")
    @Nested
    class 로그아웃 {

        @Test
        void 로그아웃_성공시_200_OK() throws Exception {
            // when
            ApiResponse<String> response = authMockApiCaller.logout(token, 200);

            // then
            assertThat(response.getResultCode()).isEmpty();
            assertThat(response.getMessage()).isEmpty();
            assertThat(response.getData()).isEqualTo(ApiResponse.SUCCESS.getData());
        }

    }

}
