package com.depromeet.threedollar.api.userservice.controller.user;

import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse;
import com.depromeet.threedollar.api.userservice.SetupUserControllerTest;
import com.depromeet.threedollar.api.userservice.service.user.dto.request.CheckAvailableNameRequest;
import com.depromeet.threedollar.api.userservice.service.user.dto.request.UpdateUserInfoRequest;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.Medal;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedalFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends SetupUserControllerTest {

    @Autowired
    private UserMedalRepository userMedalRepository;

    @Autowired
    private MedalRepository medalRepository;

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

            Medal medal = MedalFixture.create(medalName, description, activationIconUrl, disableIconUrl);
            medalRepository.save(medal);
            userMedalRepository.save(UserMedalFixture.create(medal, user));

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
        void 나의_회원정보를_수정한다() throws Exception {
            // given
            String name = "디프만";
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
        void 사용가능한_닉네임인지_확인한다() throws Exception {
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

        private ResultActions checkAvailableNickNameApi(CheckAvailableNameRequest request) throws Exception {
            return mockMvc.perform(get("/v2/user/name/check")
                .param("name", request.getName()));
        }

    }

}
