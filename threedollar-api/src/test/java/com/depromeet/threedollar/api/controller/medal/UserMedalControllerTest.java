package com.depromeet.threedollar.api.controller.medal;

import com.depromeet.threedollar.api.controller.SetupUserControllerTest;
import com.depromeet.threedollar.api.service.medal.dto.request.ActivateUserMedalRequest;
import com.depromeet.threedollar.api.service.medal.dto.response.UserMedalResponse;
import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.domain.domain.medal.UserMedalCreator;
import com.depromeet.threedollar.domain.domain.medal.UserMedalRepository;
import com.depromeet.threedollar.domain.domain.medal.UserMedalType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.depromeet.threedollar.common.exception.ErrorCode.NOT_FOUND_MEDAL_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserMedalControllerTest extends SetupUserControllerTest {

    private UserMedalMockApiCaller userMedalMockApiCaller;

    @BeforeEach
    void setUp() {
        userMedalMockApiCaller = new UserMedalMockApiCaller(mockMvc, objectMapper);
    }

    @Autowired
    private UserMedalRepository userMedalRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        userMedalRepository.deleteAll();
    }

    @DisplayName("사용자가 보유중인 메달들을 조회한다")
    @Nested
    class getAvailableUserMedals {

        @EnumSource
        @ParameterizedTest
        void 보유중인_메달들을_모두_조회한다(UserMedalType medalType) throws Exception {
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

        @Test
        void 다른_사람이_보유한_메달들은_조회되지_않는다() throws Exception {
            // given
            Long anotherUserId = 99999L;
            userMedalRepository.save(UserMedalCreator.create(anotherUserId, UserMedalType.BUNGEOPPANG_CHALLENGER));

            // when
            ApiResponse<List<UserMedalResponse>> response = userMedalMockApiCaller.getAvailableUserMedals(token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).isEmpty()
            );
        }

    }

    @DisplayName("사용자의 장착중인 메달을 수정한다")
    @Nested
    class activateUserMedal {

        @EnumSource
        @ParameterizedTest
        void 유저가_보유중인_메달을_교체한다(UserMedalType medalType) throws Exception {
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
        void 메달_타입이_NULL인경우_아무런_메달을_장착하지_않는다(UserMedalType medalType) throws Exception {
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

        @EnumSource
        @ParameterizedTest
        void 보유중이지_않은_메달을_장착_요청시_404_에러가_발생한다(UserMedalType medalType) throws Exception {
            // given
            ActivateUserMedalRequest request = ActivateUserMedalRequest.testInstance(medalType);

            // when
            ApiResponse<UserInfoResponse> response = userMedalMockApiCaller.activateUserMedal(request, token, 404);

            // then
            assertAll(
                () -> assertThat(response.getResultCode()).isEqualTo(NOT_FOUND_MEDAL_EXCEPTION.getCode()),
                () -> assertThat(response.getMessage()).isEqualTo(NOT_FOUND_MEDAL_EXCEPTION.getMessage())
            );
        }

    }

}
