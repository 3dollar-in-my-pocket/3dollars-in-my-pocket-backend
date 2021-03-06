package com.depromeet.threedollar.api.userservice.controller.medal;

import com.depromeet.threedollar.api.userservice.SetupUserControllerTest;
import com.depromeet.threedollar.api.userservice.service.medal.dto.request.ChangeRepresentativeMedalRequest;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.Medal;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedalFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedalRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedalStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MedalRepository medalRepository;

    @Autowired
    private UserMedalRepository userMedalRepository;

    @DisplayName("GET /api/v1/user/medals")
    @Nested
    class GetMyObtainedMedalsApiTest {

        @Test
        void 유저가_보유중인_메달을_모두_조회합니다() throws Exception {
            // given.
            Medal medalActive = MedalFixture.create("활성화중인 메달", "활성중인 메달 설명", "https://active-medal.png", "https://disable-medal.png");
            Medal medalInActive = MedalFixture.create("비활성화중인 메달", "비활성화중인 메달 설명", "https://active-medal-two.png", "https://disable-medal-two.png");
            medalRepository.saveAll(List.of(medalActive, medalInActive));

            userMedalRepository.saveAll(List.of(
                UserMedalFixture.create(medalActive, user),
                UserMedalFixture.create(medalInActive, user, UserMedalStatus.IN_ACTIVE)
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
        void 유저가_대표_메달을_변경합니다() throws Exception {
            // given
            Medal medal = MedalFixture.create("활성화중인 메달", "활성중인 메달 설명", "https://active-medal.jpeg", "https://disable-medal.jpeg");
            medalRepository.save(medal);

            userMedalRepository.save(UserMedalFixture.create(medal, user, UserMedalStatus.IN_ACTIVE));

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
