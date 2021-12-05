package com.depromeet.threedollar.api.service.user;

import com.depromeet.threedollar.api.service.SetupUserServiceTest;
import com.depromeet.threedollar.api.service.user.dto.request.ActivateRepresentativeMedalRequest;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.domain.medal.*;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.depromeet.threedollar.api.assertutils.assertUserUtils.assertUserMedal;
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
        void 장착중인_대표_칭호를_변경한다(String name, String iconUrl) {
            // given
            Medal medal = MedalCreator.create(name, iconUrl);
            medalRepository.save(medal);

            UserMedal userMedal = UserMedalCreator.createInActive(medal, user);
            userMedalRepository.save(userMedal);

            ActivateRepresentativeMedalRequest request = ActivateRepresentativeMedalRequest.testInstance(userMedal.getId());

            // when
            userMedalService.updateRepresentativeMedal(request, userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertAll(
                () -> assertThat(userMedals).hasSize(1),
                () -> assertUserMedal(userMedals.get(0), user.getId(), medal.getId(), UserMedalStatus.ACTIVE)
            );
        }

        @Test
        void 대표_칭호_변경시_보유하지_않은_칭호를_장착하려하면_NotFound_에러가_발생한다() {
            // given
            Long notFoundMedalId = -1L;
            ActivateRepresentativeMedalRequest request = ActivateRepresentativeMedalRequest.testInstance(notFoundMedalId);

            // when & then
            assertThatThrownBy(() -> userMedalService.updateRepresentativeMedal(request, userId)).isInstanceOfAny(NotFoundException.class);
        }

        @AutoSource
        @ParameterizedTest
        void 대표_칭호_변경시_메달_종류를_NULL을_요청하면_대표_칭호가_비활성화된다(String name, String iconUrl) {
            // given
            Medal medal = MedalCreator.create(name, iconUrl);
            medalRepository.save(medal);

            UserMedal userMedal = UserMedalCreator.createActive(medal, user);
            userMedalRepository.save(userMedal);

            ActivateRepresentativeMedalRequest request = ActivateRepresentativeMedalRequest.testInstance(null);

            // when
            userMedalService.updateRepresentativeMedal(request, userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertAll(
                () -> assertThat(userMedals).hasSize(1),
                () -> assertUserMedal(userMedals.get(0), userId, medal.getId(), UserMedalStatus.IN_ACTIVE)
            );
        }

    }

}
