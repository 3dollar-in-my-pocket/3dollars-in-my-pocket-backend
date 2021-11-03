package com.depromeet.threedollar.api.controller.user;

import com.depromeet.threedollar.api.controller.SetupUserControllerTest;
import com.depromeet.threedollar.api.service.user.dto.request.CheckAvailableNameRequest;
import com.depromeet.threedollar.api.service.user.dto.request.UpdateUserInfoRequest;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.domain.domain.user.UserCreator;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.depromeet.threedollar.common.exception.ErrorCode.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends SetupUserControllerTest {

    @AfterEach
    void cleanUp() {
        super.cleanup();
    }

    @DisplayName("GET /api/v2/user/me")
    @Nested
    class 회원_정보_조회 {

        @Test
        void 나의_회원정보_조회시_정상적으로_회원정보가_조회된다() throws Exception {
            // when & then
            getUserInfoApi(token)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(testUser.getId()))
                .andExpect(jsonPath("$.data.name").value(testUser.getName()))
                .andExpect(jsonPath("$.data.socialType").value(testUser.getSocialType().name()));
        }

        @Test
        void 잘못된_세션이면_401_에러() throws Exception {
            // given
            String token = "Wrong Token";

            // when & then
            getUserInfoApi(token)
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.resultCode").value(UNAUTHORIZED_EXCEPTION.getCode()))
                .andExpect(jsonPath("$.message").value(UNAUTHORIZED_EXCEPTION.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
        }

        private ResultActions getUserInfoApi(String token) throws Exception {
            return mockMvc.perform(get("/api/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, token));
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

            // when & then
            updateUserInfoApi(token, request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(testUser.getId()))
                .andExpect(jsonPath("$.data.name").value(name))
                .andExpect(jsonPath("$.data.socialType").value(testUser.getSocialType().name()));
        }

        @Test
        void 나의_회원정보_수정_요청시_닉네임이_중복되는경우_409_에러() throws Exception {
            // given
            String name = "디프만";
            userRepository.save(UserCreator.create("social-social-id", UserSocialType.APPLE, name));

            UpdateUserInfoRequest request = UpdateUserInfoRequest.testInstance(name);

            // when & then
            updateUserInfoApi(token, request)
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.resultCode").value(CONFLICT_EXCEPTION.getCode()))
                .andExpect(jsonPath("$.message").value(CONFLICT_EXCEPTION.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
        }

        @Test
        void 나의_회원정보_수정_요청시_잘못된_세션일경우_401_에러() throws Exception {
            // given
            String token = "Wrong Token";
            UpdateUserInfoRequest request = UpdateUserInfoRequest.testInstance("디프만");

            // when & then
            updateUserInfoApi(token, request)
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.resultCode").value(UNAUTHORIZED_EXCEPTION.getCode()))
                .andExpect(jsonPath("$.message").value(UNAUTHORIZED_EXCEPTION.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
        }

        private ResultActions updateUserInfoApi(String token, UpdateUserInfoRequest request) throws Exception {
            return mockMvc.perform(put("/api/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
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

            // when & then
            checkAvailableNickNameApi(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(ApiResponse.SUCCESS.getData()));
        }

        @Test
        void 사용가능한_닉네임_확인_요청시_중복된_이름인경우_409_에러() throws Exception {
            // given
            String name = "디프만";
            userRepository.save(UserCreator.create("social-social-id", UserSocialType.APPLE, name));
            CheckAvailableNameRequest request = CheckAvailableNameRequest.testInstance(name);

            // when & then
            checkAvailableNickNameApi(request)
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.resultCode").value(CONFLICT_EXCEPTION.getCode()))
                .andExpect(jsonPath("$.message").value(CONFLICT_EXCEPTION.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
        }

        @ParameterizedTest
        @ValueSource(strings = {"-a-", "a--", "디", "디#프만", "디+프만"})
        void 사용가능한_닉네임_확인_요청시_허용되지_않은_닉네임인경우_400_에러(String name) throws Exception {
            // given
            userRepository.save(UserCreator.create("social-social-id", UserSocialType.APPLE, name));
            CheckAvailableNameRequest request = CheckAvailableNameRequest.testInstance(name);

            // when & then
            checkAvailableNickNameApi(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value(VALIDATION_EXCEPTION.getCode()))
                .andExpect(jsonPath("$.data").isEmpty());
        }

        private ResultActions checkAvailableNickNameApi(CheckAvailableNameRequest request) throws Exception {
            return mockMvc.perform(get("/api/v2/user/name/check")
                .param("name", request.getName()));
        }

    }

}
