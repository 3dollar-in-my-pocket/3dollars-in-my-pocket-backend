package com.depromeet.threedollar.api.controller.auth;

import com.depromeet.threedollar.api.controller.SetupUserControllerTest;
import com.depromeet.threedollar.api.service.auth.AppleAuthService;
import com.depromeet.threedollar.api.service.auth.GoogleAuthService;
import com.depromeet.threedollar.api.service.auth.KaKaoAuthService;
import com.depromeet.threedollar.api.service.auth.dto.request.LoginRequest;
import com.depromeet.threedollar.api.service.auth.dto.request.SignUpRequest;
import com.depromeet.threedollar.api.service.auth.dto.response.LoginResponse;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class AuthControllerTest extends SetupUserControllerTest {

    private AuthMockApiCaller authMockApiCaller;

    @MockBean
    private KaKaoAuthService kaKaoAuthService;

    @MockBean
    private AppleAuthService appleAuthService;

    @MockBean
    private GoogleAuthService googleAuthService;

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
    class SignUp {

        @Test
        void 카카오_회원가입_요청이_성공하면_토큰이_반환된다() throws Exception {
            // given
            SignUpRequest request = SignUpRequest.testInstance("token", "will", UserSocialType.KAKAO);

            when(kaKaoAuthService.signUp(request)).thenReturn(testUser.getId());

            // when
            ApiResponse<LoginResponse> response = authMockApiCaller.signUp(request, 200);

            // then
            assertThat(response.getResultCode()).isEmpty();
            assertThat(response.getMessage()).isEmpty();
            assertThat(response.getData().getToken()).isNotNull();
        }

        @Test
        void 애플_회원가입_요청이_성공하면_토큰이_반환된다() throws Exception {
            // given
            SignUpRequest request = SignUpRequest.testInstance("token", "will", UserSocialType.APPLE);

            when(appleAuthService.signUp(request)).thenReturn(testUser.getId());

            // when
            ApiResponse<LoginResponse> response = authMockApiCaller.signUp(request, 200);

            // then
            assertThat(response.getResultCode()).isEmpty();
            assertThat(response.getMessage()).isEmpty();
            assertThat(response.getData().getToken()).isNotNull();
        }

        @Test
        void 구글_회원가입_요청이_성공하면_토큰이_반환된다() throws Exception {
            // given
            SignUpRequest request = SignUpRequest.testInstance("token", "will", UserSocialType.GOOGLE);

            when(googleAuthService.signUp(request)).thenReturn(testUser.getId());

            // when
            ApiResponse<LoginResponse> response = authMockApiCaller.signUp(request, 200);

            // then
            assertThat(response.getResultCode()).isEmpty();
            assertThat(response.getMessage()).isEmpty();
            assertThat(response.getData().getToken()).isNotNull();
        }

    }

    @DisplayName("POST /api/v2/login")
    @Nested
    class 로그인 {

        @Test
        void 카카오_로그인_요청이_성공하면_토큰이_반환된다() throws Exception {
            // given
            LoginRequest request = LoginRequest.testInstance("token", UserSocialType.KAKAO);

            when(kaKaoAuthService.login(request)).thenReturn(testUser.getId());

            // when
            ApiResponse<LoginResponse> response = authMockApiCaller.login(request, 200);

            // then
            assertThat(response.getResultCode()).isEmpty();
            assertThat(response.getMessage()).isEmpty();
            assertThat(response.getData().getToken()).isNotNull();
        }

        @Test
        void 애플_로그인_요청이_성공하면_토큰이_반환된다() throws Exception {
            // given
            LoginRequest request = LoginRequest.testInstance("token", UserSocialType.APPLE);

            when(appleAuthService.login(request)).thenReturn(testUser.getId());

            // when
            ApiResponse<LoginResponse> response = authMockApiCaller.login(request, 200);

            // then
            assertThat(response.getResultCode()).isEmpty();
            assertThat(response.getMessage()).isEmpty();
            assertThat(response.getData().getToken()).isNotNull();
        }

        @Test
        void 구글_로그인_요청이_성공하면_토큰이_반환된다() throws Exception {
            // given
            LoginRequest request = LoginRequest.testInstance("token", UserSocialType.GOOGLE);

            when(googleAuthService.login(request)).thenReturn(testUser.getId());

            // when
            ApiResponse<LoginResponse> response = authMockApiCaller.login(request, 200);

            // then
            assertThat(response.getResultCode()).isEmpty();
            assertThat(response.getMessage()).isEmpty();
            assertThat(response.getData().getToken()).isNotNull();
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
        void 성공하면_200_OK() throws Exception {
            // when
            ApiResponse<String> response = authMockApiCaller.logout(token, 200);

            // then
            assertThat(response.getResultCode()).isEmpty();
            assertThat(response.getMessage()).isEmpty();
            assertThat(response.getData()).isEqualTo(ApiResponse.SUCCESS.getData());
        }

    }

}
