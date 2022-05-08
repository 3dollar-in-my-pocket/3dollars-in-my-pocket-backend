package com.depromeet.threedollar.api.user.controller.user;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.CONFLICT_NICKNAME;
import static com.depromeet.threedollar.common.exception.type.ErrorCode.INVALID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.depromeet.threedollar.api.core.common.dto.ApiResponse;
import com.depromeet.threedollar.api.user.controller.SetupUserControllerTest;
import com.depromeet.threedollar.api.user.service.user.dto.request.CheckAvailableNameRequest;
import com.depromeet.threedollar.api.user.service.user.dto.request.UpdateUserInfoRequest;
import com.depromeet.threedollar.domain.rds.user.domain.medal.Medal;
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalCreator;
import com.depromeet.threedollar.domain.rds.user.domain.medal.UserMedal;
import com.depromeet.threedollar.domain.rds.user.domain.medal.UserMedalCreator;
import com.depromeet.threedollar.domain.rds.user.domain.user.UserCreator;
import com.depromeet.threedollar.domain.rds.user.domain.user.UserSocialType;

class UserControllerTest extends SetupUserControllerTest {

    @AfterEach
    void cleanUp() {
        super.cleanup();
    }

    @DisplayName("GET /api/v2/user/me")
    @Nested
    class GetUserAccountInfoApiTest {

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

            Medal medal = MedalCreator.builder()
                .name(medalName)
                .acquisitionDescription(description)
                .activationIconUrl(activationIconUrl)
                .disableIconUrl(disableIconUrl)
                .build();
            medalRepository.save(medal);
            UserMedal userMedal = UserMedalCreator.active()
                .medal(medal)
                .user(user)
                .build();
            userMedalRepository.save(userMedal);

            // when & then
            getUserInfoApi(token)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.medal.name").value(medalName))
                .andExpect(jsonPath("$.data.medal.iconUrl").value(activationIconUrl))
                .andExpect(jsonPath("$.data.medal.disableIconUrl").value(disableIconUrl));
        }

        private ResultActions getUserInfoApi(String token) throws Exception {
            return mockMvc.perform(get("/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, token));
        }

    }

    @DisplayName("PUT /api/v2/user/me")
    @Nested
    class UpdateUserAccountInfoApiTest {

        @Test
        void 나의_회원정보_수정_요청시_회원정보가_정상적으로_수정된다() throws Exception {
            // given
            String name = "가삼";
            UpdateUserInfoRequest request = UpdateUserInfoRequest.testBuilder()
                .name(name)
                .build();

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
    class CheckAvailableNicknameApiTest {

        @Test
        void 사용가능한_닉네임_확인_요청시_사용가능한_닉네임이면_200_OK() throws Exception {
            // given
            String name = "붕어빵";

            CheckAvailableNameRequest request = CheckAvailableNameRequest.testBuilder()
                .name(name)
                .build();

            // when & then
            checkAvailableNickNameApi(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(ApiResponse.OK.getData()));
        }

        @Test
        void 사용가능한_닉네임_확인_요청시_중복된_이름인경우_409_에러() throws Exception {
            // given
            String name = "토수니";
            userRepository.save(UserCreator.builder()
                .socialId("social-id")
                .socialType(UserSocialType.APPLE)
                .name(name)
                .build());

            CheckAvailableNameRequest request = CheckAvailableNameRequest.testBuilder()
                .name(name)
                .build();

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
            UserCreator.builder()
                .socialId("social-id")
                .socialType(UserSocialType.APPLE)
                .name(name)
                .build();
            userRepository.save(user);

            CheckAvailableNameRequest request = CheckAvailableNameRequest.testBuilder()
                .name(name)
                .build();

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
