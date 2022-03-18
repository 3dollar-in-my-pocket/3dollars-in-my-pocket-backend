package com.depromeet.threedollar.api.user.controller.user;

import com.depromeet.threedollar.api.user.controller.SetupUserControllerTest;
import com.depromeet.threedollar.api.user.service.user.dto.request.CheckAvailableNameRequest;
import com.depromeet.threedollar.api.user.service.user.dto.request.UpdateUserInfoRequest;
import com.depromeet.threedollar.api.core.common.dto.ApiResponse;
import com.depromeet.threedollar.domain.rds.user.domain.medal.Medal;
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalCreator;
import com.depromeet.threedollar.domain.rds.user.domain.medal.UserMedalCreator;
import com.depromeet.threedollar.domain.rds.user.domain.user.UserCreator;
import com.depromeet.threedollar.domain.rds.user.domain.user.UserSocialType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.*;
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
                .andExpect(jsonPath("$.data.userId").value(user.getId()))
                .andExpect(jsonPath("$.data.name").value(user.getName()))
                .andExpect(jsonPath("$.data.socialType").value(user.getSocialType().name()));
        }

        @Test
        void 나의_회원정보_조회시_활성화중인_메달이_있는경우_함께_조회된다() throws Exception {
            // given
            String medalName = "붕어빵 전문가";
            String description = "우리 동네 붕어빵에 대해서는 내가 바로 척척박사";
            String activationIconUrl = "https://activation-icon.png";
            String disableIconUrl = "https://disable-icon.png";

            Medal medal = MedalCreator.create(medalName, description, activationIconUrl, disableIconUrl);
            medalRepository.save(medal);
            userMedalRepository.save(UserMedalCreator.createActive(medal, user));

            // when & then
            getUserInfoApi(token)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.medal.name").value(medalName))
                .andExpect(jsonPath("$.data.medal.iconUrl").value(activationIconUrl))
                .andExpect(jsonPath("$.data.medal.disableIconUrl").value(disableIconUrl));
        }


        @Test
        void 잘못된_세션이면_401_에러() throws Exception {
            // given
            String token = "Wrong Token";

            // when & then
            getUserInfoApi(token)
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.resultCode").value(UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.message").value(UNAUTHORIZED.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
        }

        private ResultActions getUserInfoApi(String token) throws Exception {
            return mockMvc.perform(get("/v2/user/me")
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
                .andExpect(jsonPath("$.data.userId").value(user.getId()))
                .andExpect(jsonPath("$.data.name").value(name))
                .andExpect(jsonPath("$.data.socialType").value(user.getSocialType().name()));
        }

        private ResultActions updateUserInfoApi(String token, UpdateUserInfoRequest request) throws Exception {
            return mockMvc.perform(put("/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        }

    }

    @DisplayName("GET /api/v2/user/me/name/check")
    @Nested
    class 사용가능한_닉네임_체크 {

        @Test
        void 사용가능한_닉네임_확인_요청시_사용가능한_닉네임이면_200_OK() throws Exception {
            String name = "붕어빵";

            // given
            CheckAvailableNameRequest request = CheckAvailableNameRequest.testInstance(name);

            // when & then
            checkAvailableNickNameApi(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(ApiResponse.OK.getData()));
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
                .andExpect(jsonPath("$.resultCode").value(CONFLICT_NICKNAME.getCode()))
                .andExpect(jsonPath("$.message").value(CONFLICT_NICKNAME.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
        }

        @Test
        void 사용가능한_닉네임_확인_요청시_허용되지_않은_닉네임인경우_400_에러() throws Exception {
            // given
            String name = "-a-";
            userRepository.save(UserCreator.create("social-social-id", UserSocialType.APPLE, name));
            CheckAvailableNameRequest request = CheckAvailableNameRequest.testInstance(name);

            // when & then
            checkAvailableNickNameApi(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value(INVALID.getCode()))
                .andExpect(jsonPath("$.data").isEmpty());
        }

        private ResultActions checkAvailableNickNameApi(CheckAvailableNameRequest request) throws Exception {
            return mockMvc.perform(get("/v2/user/name/check")
                .param("name", request.getName()));
        }

    }

}
