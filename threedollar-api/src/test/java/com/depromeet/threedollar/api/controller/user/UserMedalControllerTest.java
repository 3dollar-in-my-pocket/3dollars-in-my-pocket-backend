package com.depromeet.threedollar.api.controller.user;

import com.depromeet.threedollar.api.controller.SetupUserControllerTest;
import com.depromeet.threedollar.domain.domain.medal.Medal;
import com.depromeet.threedollar.domain.domain.medal.MedalCreator;
import com.depromeet.threedollar.domain.domain.medal.UserMedalCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    class 보유중인_훈장을_조회한다 {

        @Test
        void 보유중인_칭호들을_모두_조회한다() throws Exception {
            // given
            Medal medalActive = MedalCreator.create("활성화중인 메달", "메달 아이콘 A");
            Medal medalInActive = MedalCreator.create("비활성화중인 메달", "메달 아이콘 B");
            medalRepository.saveAll(List.of(medalActive, medalInActive));

            userMedalRepository.saveAll(List.of(
                UserMedalCreator.createActive(medalActive, testUser),
                UserMedalCreator.createActive(medalInActive, testUser)
            ));

            // when & then
            retrieveMyMedalsObtained(token)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].userMedalId").value(medalActive.getId()))
                .andExpect(jsonPath("$.data[0].name").value(medalActive.getName()))
                .andExpect(jsonPath("$.data[0].iconUrl").value(medalActive.getIconUrl()))
                .andExpect(jsonPath("$.data[1].userMedalId").value(medalInActive.getId()))
                .andExpect(jsonPath("$.data[1].name").value(medalInActive.getName()))
                .andExpect(jsonPath("$.data[1].iconUrl").value(medalInActive.getIconUrl()));
        }

        private ResultActions retrieveMyMedalsObtained(String token) throws Exception {
            return mockMvc.perform(get("/api/v1/user/medals")
                .header(HttpHeaders.AUTHORIZATION, token));
        }

    }

}
