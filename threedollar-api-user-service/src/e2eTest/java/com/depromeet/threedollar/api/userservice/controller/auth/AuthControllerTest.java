package com.depromeet.threedollar.api.userservice.controller.auth;

import com.depromeet.threedollar.api.core.common.dto.ApiResponse;
import com.depromeet.threedollar.api.userservice.SetupUserControllerTest;
import com.depromeet.threedollar.api.core.listener.commonservice.device.DeviceEventListener;
import com.depromeet.threedollar.api.userservice.service.auth.dto.request.LoginRequest;
import com.depromeet.threedollar.api.userservice.service.auth.dto.request.SignUpRequest;
import com.depromeet.threedollar.api.userservice.service.auth.dto.response.LoginResponse;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserSocialType;
import com.depromeet.threedollar.domain.rds.event.userservice.user.UserLogOutedEvent;
import com.depromeet.threedollar.domain.rds.event.userservice.user.UserSignOutedEvent;
import com.depromeet.threedollar.infrastructure.external.client.apple.AppleTokenDecoder;
import com.depromeet.threedollar.infrastructure.external.client.google.GoogleAuthApiClient;
import com.depromeet.threedollar.infrastructure.external.client.google.dto.response.GoogleProfileInfoResponse;
import com.depromeet.threedollar.infrastructure.external.client.kakao.KaKaoAuthApiClient;
import com.depromeet.threedollar.infrastructure.external.client.kakao.dto.response.KaKaoProfileResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthControllerTest extends SetupUserControllerTest {

    private static final String KAKAO_SOCIAL_ID = "kakao-social-id";
    private static final String GOOGLE_SOCIAL_ID = "google-social-id";
    private static final String APPLE_SOCIAL_ID = "apple-social-id";

    private AuthMockApiCaller authMockApiCaller;

    @MockBean
    private KaKaoAuthApiClient kaKaoAuthApiClient;

    @MockBean
    private AppleTokenDecoder appleTokenDecoder;

    @MockBean
    private GoogleAuthApiClient googleAuthApiClient;

    @MockBean
    private DeviceEventListener deviceEventListener;

    @BeforeEach
    void setUp() {
        authMockApiCaller = new AuthMockApiCaller(mockMvc, objectMapper);
    }

    @BeforeEach
    void mockAuthApiClient() {
        when(kaKaoAuthApiClient.getProfileInfo(anyString())).thenReturn(KaKaoProfileResponse.testInstance(KAKAO_SOCIAL_ID));
        when(appleTokenDecoder.getSocialIdFromIdToken(anyString())).thenReturn(APPLE_SOCIAL_ID);
        when(googleAuthApiClient.getProfileInfo(anyString())).thenReturn(GoogleProfileInfoResponse.testInstance(GOOGLE_SOCIAL_ID));
    }

    @Nested
    class KaKaoAuthApiTest {

        @Test
        void 카카오_회원가입_요청이_성공하면_인증_토큰이_반환된다() throws Exception {
            // given
            SignUpRequest request = SignUpRequest.testBuilder()
                .token("kakao-token")
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
        void 카카오_로그인_요청이_성공하면_인증_토큰이_반환된다() throws Exception {
            // given
            User user = UserFixture.create(KAKAO_SOCIAL_ID, UserSocialType.KAKAO, "카카오 계정");
            userRepository.save(user);

            LoginRequest request = LoginRequest.testBuilder()
                .token("kakao-token")
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

    }

    @Nested
    class AppleAuthApiTest {

        @Test
        void 애플_회원가입_요청이_성공하면_인증_토큰이_반환된다() throws Exception {
            // given
            SignUpRequest request = SignUpRequest.testBuilder()
                .token("apple-token")
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
        void 애플_로그인_요청이_성공하면_인증_토큰이_반환된다() throws Exception {
            // given
            User user = UserFixture.create(APPLE_SOCIAL_ID, UserSocialType.APPLE, "애플 계정");
            userRepository.save(user);

            LoginRequest request = LoginRequest.testBuilder()
                .token("apple-token")
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

    }

    @Nested
    class GoogleAuthApiTest {

        @Test
        void 구글_회원가입_요청이_성공하면_인증_토큰이_반환된다() throws Exception {
            // given
            SignUpRequest request = SignUpRequest.testBuilder()
                .token("google-token")
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

        @Test
        void 구글_로그인_요청이_성공하면_인증_토큰이_반환된다() throws Exception {
            // given
            User user = UserFixture.create(GOOGLE_SOCIAL_ID, UserSocialType.GOOGLE, "구글 계정");
            userRepository.save(user);

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

        @Test
        void 로그아웃시_디바이스_삭제_이벤트가_발송된다() throws Exception {
            // when
            authMockApiCaller.signOut(token, 200);

            // then
            verify(deviceEventListener, times(1)).deleteUserDevice(any(UserSignOutedEvent.class));
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

        @Test
        void 로그아웃시_디바이스_삭제_이벤트가_발송된다() throws Exception {
            // when
            authMockApiCaller.logout(token, 200);

            // then
            verify(deviceEventListener, times(1)).deleteUserDevice(any(UserLogOutedEvent.class));
        }

    }

}
