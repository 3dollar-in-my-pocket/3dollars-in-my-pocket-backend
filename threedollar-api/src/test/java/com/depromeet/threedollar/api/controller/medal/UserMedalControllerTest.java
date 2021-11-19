package com.depromeet.threedollar.api.controller.medal;

import com.depromeet.threedollar.api.controller.SetupUserControllerTest;
import com.depromeet.threedollar.api.service.medal.dto.request.ActivateUserMedalRequest;
import com.depromeet.threedollar.api.service.medal.dto.response.UserMedalResponse;
import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.domain.domain.medal.UserMedalCreator;
import com.depromeet.threedollar.domain.domain.medal.UserMedalType;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserMedalControllerTest extends SetupUserControllerTest {

    private UserMedalMockApiCaller userMedalMockApiCaller;

    @BeforeEach
    void setUp() {
        userMedalMockApiCaller = new UserMedalMockApiCaller(mockMvc, objectMapper);
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
    }

    @DisplayName("사용자가 보유중인 칭호들을 조회한다")
    @Nested
    class getAvailableUserMedals {

        @AutoSource
        @ParameterizedTest
        void 보유중인_칭호들을_모두_조회한다(UserMedalType medalType) throws Exception {
            // given
            userMedalRepository.save(UserMedalCreator.create(testUser.getId(), medalType));

            // when
            ApiResponse<List<UserMedalResponse>> response = userMedalMockApiCaller.getAvailableUserMedals(token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).hasSize(1),
                () -> assertThat(response.getData().get(0).getMedalType()).isEqualTo(medalType)
            );
        }

    }

    @DisplayName("사용자의 장착중인 대표 메달을 변경한다")
    @Nested
    class activateUserMedal {

        @AutoSource
        @ParameterizedTest
        void 유저가_장착중인_대표_칭호을_변경한다(UserMedalType medalType) throws Exception {
            // given
            userMedalRepository.save(UserMedalCreator.create(testUser.getId(), medalType));
            ActivateUserMedalRequest request = ActivateUserMedalRequest.testInstance(medalType);

            // when
            ApiResponse<UserInfoResponse> response = userMedalMockApiCaller.activateUserMedal(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getUserId()).isEqualTo(testUser.getId()),
                () -> assertThat(response.getData().getName()).isEqualTo(testUser.getName()),
                () -> assertThat(response.getData().getSocialType()).isEqualTo(testUser.getSocialType()),
                () -> assertThat(response.getData().getMedalType()).isEqualTo(medalType)
            );
        }

        @NullSource
        @ParameterizedTest
        void 대표_칭호를_변경시_메달_타입이_NULL인경우_아무런_메달을_장착하지_않는다(UserMedalType medalType) throws Exception {
            // given
            ActivateUserMedalRequest request = ActivateUserMedalRequest.testInstance(medalType);

            // when
            ApiResponse<UserInfoResponse> response = userMedalMockApiCaller.activateUserMedal(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getUserId()).isEqualTo(testUser.getId()),
                () -> assertThat(response.getData().getName()).isEqualTo(testUser.getName()),
                () -> assertThat(response.getData().getSocialType()).isEqualTo(testUser.getSocialType()),
                () -> assertThat(response.getData().getMedalType()).isEqualTo(medalType)
            );
        }

    }

}
