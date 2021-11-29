package com.depromeet.threedollar.api.service.medal;

import com.depromeet.threedollar.api.service.SetupUserServiceTest;
import com.depromeet.threedollar.api.service.medal.dto.request.ActivateUserMedalRequest;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.domain.medal.UserMedalCreator;
import com.depromeet.threedollar.domain.domain.medal.UserMedalType;
import com.depromeet.threedollar.domain.domain.user.User;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class UserMedalServiceTest extends SetupUserServiceTest {

    @Autowired
    private UserMedalService userMedalService;

    @AfterEach
    void cleanUp() {
        super.cleanup();
    }

    @DisplayName("유저의 장착중인 대표 칭호를 변경한다")
    @Nested
    class ActivateUserMedal {

        @AutoSource
        @ParameterizedTest
        void 장착중인_대표_칭호를_변경한다(UserMedalType medalType) {
            // given
            userMedalRepository.save(UserMedalCreator.create(userId, medalType));
            ActivateUserMedalRequest request = ActivateUserMedalRequest.testInstance(medalType);

            // when
            userMedalService.activateUserMedal(request, userId);

            // then
            List<User> users = userRepository.findAll();
            assertAll(
                () -> assertThat(users).hasSize(1),
                () -> assertThat(users.get(0).getMedalType()).isEqualTo(medalType)
            );
        }

        @AutoSource
        @ParameterizedTest
        void 대표_칭호_변경시_보유하지_않은_칭호를_장착하려하면_NotFound_에러가_발생한다(UserMedalType medalType) {
            // given
            ActivateUserMedalRequest request = ActivateUserMedalRequest.testInstance(medalType);

            // when & then
            assertThatThrownBy(() -> userMedalService.activateUserMedal(request, userId)).isInstanceOfAny(NotFoundException.class);
        }

        @NullSource
        @ParameterizedTest
        void 대표_칭호_변경시_메달_종류를_NULL을_요청하면_대표_칭호가_비활성화된다(UserMedalType medalType) {
            // given
            ActivateUserMedalRequest request = ActivateUserMedalRequest.testInstance(medalType);

            // when
            userMedalService.activateUserMedal(request, userId);

            // then
            List<User> users = userRepository.findAll();
            assertAll(
                () -> assertThat(users).hasSize(1),
                () -> assertThat(users.get(0).getMedalType()).isNull()
            );
        }
    }

}
