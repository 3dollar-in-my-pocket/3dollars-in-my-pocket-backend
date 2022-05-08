package com.depromeet.threedollar.api.user.controller.medal;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.depromeet.threedollar.api.user.controller.SetupUserControllerTest;
import com.depromeet.threedollar.api.user.service.medal.dto.request.ChangeRepresentativeMedalRequest;
import com.depromeet.threedollar.domain.rds.user.domain.medal.Medal;
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalCreator;
import com.depromeet.threedollar.domain.rds.user.domain.medal.UserMedalCreator;

class UserMedalControllerTest extends SetupUserControllerTest {

    @AfterEach
    void cleanUp() {
        super.cleanup();
    }

    @DisplayName("GET /api/v1/user/medals")
    @Nested
    class GetMyObtainedMedalsApiTest {

        @Test
        void 보유중인_칭호들을_모두_조회한다() throws Exception {
            // given.
            Medal medalActive = MedalCreator.builder()
                .name("유저가 활성화 중인 메달")
                .activationIconUrl("http://medal-image.png")
                .disableIconUrl("http://medal-image-disable.png")
                .build();
            Medal medalInActive = MedalCreator.builder()
                .name("유저가 비활성화 중인 메달")
                .activationIconUrl("http://medal-image-two.png")
                .disableIconUrl("http://medal-image-disable=two.png")
                .build();
            medalRepository.saveAll(List.of(medalActive, medalInActive));

            userMedalRepository.saveAll(List.of(
                UserMedalCreator.createActive(medalActive, user),
                UserMedalCreator.createInActive(medalInActive, user)
            ));

            // when & then
            getMyObtainedMedals(token)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].medalId").value(medalActive.getId()))
                .andExpect(jsonPath("$.data[0].name").value(medalActive.getName()))
                .andExpect(jsonPath("$.data[0].iconUrl").value(medalActive.getActivationIconUrl()))
                .andExpect(jsonPath("$.data[0].disableIconUrl").value(medalActive.getDisableIconUrl()))

                .andExpect(jsonPath("$.data[1].medalId").value(medalInActive.getId()))
                .andExpect(jsonPath("$.data[1].name").value(medalInActive.getName()))
                .andExpect(jsonPath("$.data[1].iconUrl").value(medalInActive.getActivationIconUrl()))
                .andExpect(jsonPath("$.data[1].disableIconUrl").value(medalInActive.getDisableIconUrl()));
        }

        private ResultActions getMyObtainedMedals(String token) throws Exception {
            return mockMvc.perform(get("/v1/user/medals")
                .header(HttpHeaders.AUTHORIZATION, token));
        }

    }

    @DisplayName("PUT /api/v1/user/medal")
    @Nested
    class ChangeRepresentativeMedalTest {

        @Test
        void 장착중인_훈장을_변경한다() throws Exception {
            // given
            Medal medal = MedalCreator.builder()
                .name("유저가 활성화 중인 메달")
                .acquisitionDescription("활성화 중인 메달 설명")
                .activationIconUrl("https://active-medal.jpeg")
                .disableIconUrl("https://disable-medal.jpeg")
                .build();
            medalRepository.save(medal);

            userMedalRepository.save(UserMedalCreator.createInActive(medal, user));

            ChangeRepresentativeMedalRequest request = ChangeRepresentativeMedalRequest.testBuilder()
                .medalId(medal.getId())
                .build();

            // when & then
            changeRepresentativeMedal(request, token)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.medal.name").value(medal.getName()))
                .andExpect(jsonPath("$.data.medal.iconUrl").value(medal.getActivationIconUrl()))
                .andExpect(jsonPath("$.data.medal.disableIconUrl").value(medal.getDisableIconUrl()));
        }

        private ResultActions changeRepresentativeMedal(ChangeRepresentativeMedalRequest request, String token) throws Exception {
            return mockMvc.perform(put("/v1/user/medal")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        }

    }

}
