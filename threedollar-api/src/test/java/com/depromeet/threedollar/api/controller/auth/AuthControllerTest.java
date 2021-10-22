package com.depromeet.threedollar.api.controller.auth;

import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.api.controller.AbstractControllerTest;
import com.depromeet.threedollar.api.service.auth.AuthService;
import com.depromeet.threedollar.api.service.auth.dto.request.LoginRequest;
import com.depromeet.threedollar.api.service.auth.dto.request.SignUpRequest;
import com.depromeet.threedollar.api.service.auth.dto.response.LoginResponse;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.depromeet.threedollar.common.exception.ErrorCode.VALIDATION_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest extends AbstractControllerTest {

    private AuthMockApiCaller authMockApiCaller;

    @MockBean(name = "kaKaoAuthService")
    private AuthService kaKaoAuthService;

    @MockBean(name = "appleAuthService")
    private AuthService appleAuthService;

    @MockBean(name = "googleAuthService")
    private AuthService googleAuthService;

    @BeforeEach
    void setUp() throws Exception {
        super.setup();
        authMockApiCaller = new AuthMockApiCaller(mockMvc, objectMapper);
    }

    @DisplayName("POST /api/v2/signup")
    @Nested
    class 회원가입 {

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

        @Test
        void 토큰을_넘기지_않으면_400에러가_발생한다() throws Exception {
            // given
            SignUpRequest request = SignUpRequest.testInstance(null, "will", UserSocialType.APPLE);

            // when
            ApiResponse<LoginResponse> response = authMockApiCaller.signUp(request, 400);

            // then
            assertThat(response.getResultCode()).isEqualTo(VALIDATION_EXCEPTION.getCode());
            assertThat(response.getData()).isNull();
        }

        @Test
        void 소셜_프로바이더를_넘기지_않으면_400에러가_발생한다() throws Exception {
            // given
            SignUpRequest request = SignUpRequest.testInstance("token", "will", null);

            // when
            ApiResponse<LoginResponse> response = authMockApiCaller.signUp(request, 400);

            // then
            assertThat(response.getResultCode()).isEqualTo(VALIDATION_EXCEPTION.getCode());
            assertThat(response.getData()).isNull();
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

        @Test
        void 토큰을_넘기지_않으면_400에러가_발생한다() throws Exception {
            // given
            LoginRequest request = LoginRequest.testInstance(null, UserSocialType.APPLE);

            // when
            ApiResponse<LoginResponse> response = authMockApiCaller.login(request, 400);

            // then
            assertThat(response.getResultCode()).isEqualTo(VALIDATION_EXCEPTION.getCode());
            assertThat(response.getData()).isNull();
        }

        @Test
        void 소셜_프로바이더를_넘기지_않으면_400에러가_발생한다() throws Exception {
            // given
            LoginRequest request = LoginRequest.testInstance("token", null);

            // when
            ApiResponse<LoginResponse> response = authMockApiCaller.login(request, 400);

            // then
            assertThat(response.getResultCode()).isEqualTo(VALIDATION_EXCEPTION.getCode());
            assertThat(response.getData()).isNull();
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
