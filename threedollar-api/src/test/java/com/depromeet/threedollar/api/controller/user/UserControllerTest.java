package com.depromeet.threedollar.api.controller.user;

import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.api.controller.AbstractControllerTest;
import com.depromeet.threedollar.api.service.user.dto.request.CheckAvailableNameRequest;
import com.depromeet.threedollar.api.service.user.dto.request.UpdateUserInfoRequest;
import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.domain.domain.user.UserCreator;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.depromeet.threedollar.common.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;

class UserControllerTest extends AbstractControllerTest {

    @AfterEach
    void cleanUp() {
        super.cleanup();
    }

    @DisplayName("GET /api/v2/user/me")
    @Nested
    class 회원_정보_조회 {

        @Test
        void 나의_회원정보_조회시_정상적으로_회원정보가_조회된다() throws Exception {
            // when
            ApiResponse<UserInfoResponse> response = userMockApiCaller.getMyUserInfo(token, 200);

            // then
            assertUserInfoResponse(response.getData(), testUser.getId(), testUser.getName(), testUser.getSocialType());
        }

        @Test
        void 잘못된_세션이면_401_에러() throws Exception {
            // when
            ApiResponse<UserInfoResponse> response = userMockApiCaller.getMyUserInfo("wrong token", 401);

            // then
            assertThat(response.getResultCode()).isEqualTo(UNAUTHORIZED_EXCEPTION.getCode());
            assertThat(response.getMessage()).isEqualTo(UNAUTHORIZED_EXCEPTION.getMessage());
            assertThat(response.getData()).isNull();
        }

    }

    @DisplayName("PUT /api/v2/user/me")
    @Nested
    class 회원_정보_수정 {

        @Test
        void 나의_회원정보_수정_요청시_회원정보가_정상적으로_수정된다() throws Exception {
            // given
            String name = "디프만";

            UpdateUserInfoRequest request = UpdateUserInfoRequest.testInstance(name);

            // when
            ApiResponse<UserInfoResponse> response = userMockApiCaller.updateMyUserInfo(request, token, 200);

            // then
            assertUserInfoResponse(response.getData(), testUser.getId(), name, testUser.getSocialType());
        }

        @Test
        void 나의_회원정보_수정_요청시_닉네임이_중복되는경우_409_에러() throws Exception {
            // given
            String name = "디프만";
            userRepository.save(UserCreator.create("social-social-id", UserSocialType.APPLE, name));

            UpdateUserInfoRequest request = UpdateUserInfoRequest.testInstance(name);

            // when
            ApiResponse<UserInfoResponse> response = userMockApiCaller.updateMyUserInfo(request, token, 409);

            // then
            assertThat(response.getResultCode()).isEqualTo(CONFLICT_EXCEPTION.getCode());
            assertThat(response.getMessage()).isEqualTo(CONFLICT_EXCEPTION.getMessage());
            assertThat(response.getData()).isNull();
        }

        @Test
        void 나의_회원정보_수정_요청시_잘못된_세션일경우_401_에러() throws Exception {
            // given
            UpdateUserInfoRequest request = UpdateUserInfoRequest.testInstance("디프만");

            // when
            ApiResponse<UserInfoResponse> response = userMockApiCaller.updateMyUserInfo(request, "wrong token", 401);

            // then
            assertThat(response.getResultCode()).isEqualTo(UNAUTHORIZED_EXCEPTION.getCode());
            assertThat(response.getMessage()).isEqualTo(UNAUTHORIZED_EXCEPTION.getMessage());
            assertThat(response.getData()).isNull();
        }

    }

    @DisplayName("GET /api/v2/user/me/name/check")
    @Nested
    class 사용가능한_닉네임_체크 {

        @ParameterizedTest
        @ValueSource(strings = {"디프만", "강승호", "승호-강", "will"})
        void 사용가능한_닉네임_확인_요청시_사용가능한_닉네임이면_200_OK(String name) throws Exception {
            // given
            CheckAvailableNameRequest request = CheckAvailableNameRequest.testInstance(name);

            // when
            ApiResponse<String> response = userMockApiCaller.checkAvailableName(request, 200);

            // then
            assertThat(response.getData()).isEqualTo("OK");
        }

        @Test
        void 사용가능한_닉네임_확인_요청시_중복된_이름인경우_409_에러() throws Exception {
            // given
            String name = "디프만";
            userRepository.save(UserCreator.create("social-social-id", UserSocialType.APPLE, name));
            CheckAvailableNameRequest request = CheckAvailableNameRequest.testInstance(name);

            // when
            ApiResponse<String> response = userMockApiCaller.checkAvailableName(request, 409);

            // then
            assertThat(response.getResultCode()).isEqualTo(CONFLICT_EXCEPTION.getCode());
            assertThat(response.getMessage()).isEqualTo(CONFLICT_EXCEPTION.getMessage());
            assertThat(response.getData()).isNull();
        }

        @ParameterizedTest
        @ValueSource(strings = {"-a-", "a--", "디", "디#프만", "디+프만"})
        void 사용가능한_닉네임_확인_요청시_허용되지_않은_닉네임인경우_400_에러(String name) throws Exception {
            // given
            userRepository.save(UserCreator.create("social-social-id", UserSocialType.APPLE, name));
            CheckAvailableNameRequest request = CheckAvailableNameRequest.testInstance(name);

            // when
            ApiResponse<String> response = userMockApiCaller.checkAvailableName(request, 400);

            // then
            assertThat(response.getResultCode()).isEqualTo(VALIDATION_EXCEPTION.getCode());
            assertThat(response.getData()).isNull();
        }

    }

    private void assertUserInfoResponse(UserInfoResponse response, Long userId, String name, UserSocialType socialType) {
        assertThat(response.getUserId()).isEqualTo(userId);
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getSocialType()).isEqualTo(socialType);
    }

}
