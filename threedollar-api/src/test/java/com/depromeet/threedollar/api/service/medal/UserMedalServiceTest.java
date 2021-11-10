package com.depromeet.threedollar.api.service.medal;

import com.depromeet.threedollar.api.service.SetupUserServiceTest;
import com.depromeet.threedollar.api.service.medal.dto.request.ActivateUserMedalRequest;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.domain.medal.UserMedal;
import com.depromeet.threedollar.domain.domain.medal.UserMedalCreator;
import com.depromeet.threedollar.domain.domain.medal.UserMedalRepository;
import com.depromeet.threedollar.domain.domain.medal.UserMedalType;
import com.depromeet.threedollar.domain.domain.user.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMedalServiceTest extends SetupUserServiceTest {

    @Autowired
    private UserMedalService userMedalService;

    @Autowired
    private UserMedalRepository userMedalRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        userMedalRepository.deleteAll();
    }

    @DisplayName("유저가 보유한 메달을 추가한다")
    @Nested
    class addUserMedal {

        @EnumSource
        @ParameterizedTest
        void 유저가_보유한_메달을_추가한다(UserMedalType medalType) {
            // when
            userMedalService.addUserMedal(medalType, userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertAll(
                () -> assertThat(userMedals).hasSize(1),
                () -> assertUserMedal(userMedals.get(0), userId, medalType)
            );
        }

        @EnumSource
        @ParameterizedTest
        void 이미_보유하고_있는_메달일경우_예외가_발생하지는_않지만_중복_저장되지_않는다(UserMedalType medalType) {
            // given
            userMedalRepository.save(UserMedalCreator.create(userId, medalType));

            // when
            userMedalService.addUserMedal(medalType, userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertAll(
                () -> assertThat(userMedals).hasSize(1),
                () -> assertUserMedal(userMedals.get(0), userId, medalType)
            );
        }
    }

    @DisplayName("유저의 장착중인 메달을 변경한다")
    @Nested
    class ActivateUserMedal {

        @EnumSource
        @ParameterizedTest
        void 유저의_장착_메달을_교체하면_DB의_유저_테이블의_장착중인_메달이_변경된다(UserMedalType medalType) {
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

        @EnumSource
        @ParameterizedTest
        void 유저가_보유하지_않은_메달을_장착하려하면_NotFoundException이_발생한다(UserMedalType medalType) {
            // given
            ActivateUserMedalRequest request = ActivateUserMedalRequest.testInstance(medalType);

            // when & then
            assertThatThrownBy(() -> userMedalService.activateUserMedal(request, userId)).isInstanceOfAny(NotFoundException.class);
        }

        @NullSource
        @ParameterizedTest
        void 아무_메달도_장착하지_않도록_변경하면_medal_type이_null_이_된다(UserMedalType medalType) {
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

    private void assertUserMedal(UserMedal userMedal, Long userId, UserMedalType type) {
        assertThat(userMedal.getUserId()).isEqualTo(userId);
        assertThat(userMedal.getMedalType()).isEqualTo(type);
    }

}
