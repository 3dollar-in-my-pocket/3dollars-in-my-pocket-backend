package com.depromeet.threedollar.api.controller.medal;

import com.depromeet.threedollar.api.controller.SetupUserControllerTest;
import com.depromeet.threedollar.api.service.medal.dto.request.ChangeRepresentativeMedalRequest;
import com.depromeet.threedollar.domain.user.domain.medal.Medal;
import com.depromeet.threedollar.domain.user.domain.medal.MedalCreator;
import com.depromeet.threedollar.domain.user.domain.medal.UserMedalCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserMedalControllerTest extends SetupUserControllerTest {

    @AfterEach
    void cleanUp() {
        super.cleanup();
    }

    @DisplayName("GET /api/v1/user/medals")
    @Nested
    class GetMyObtainedMedals {

        @Test
        void 보유중인_칭호들을_모두_조회한다() throws Exception {
            // given.
            Medal medalActive = MedalCreator.create("활성화중인 메달", "활성중인 메달 설명", "메달 아이콘 A", "비활성화 아이콘 A");
            Medal medalInActive = MedalCreator.create("비활성화중인 메달", "비활성화중인 메달 설명", "메달 아이콘 B", "비활성화 아이콘 B");
            medalRepository.saveAll(List.of(medalActive, medalInActive));

            userMedalRepository.saveAll(List.of(
                UserMedalCreator.createActive(medalActive, testUser),
                UserMedalCreator.createInActive(medalInActive, testUser)
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
    class ChangeRepresentaiveMedal {

        @Test
        void 장착중인_훈장을_변경한다() throws Exception {
            // given
            Medal medal = MedalCreator.create("활성화중인 메달", "활성중인 메달 설명", "메달 아이콘 A", "비활성화 아이콘 A");
            medalRepository.save(medal);

            userMedalRepository.save(UserMedalCreator.createInActive(medal, testUser));

            // when & then
            changeRepresentativeMedal(ChangeRepresentativeMedalRequest.testInstance(medal.getId()), token)
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
