package com.depromeet.threedollar.api.user.service.medal;

import com.depromeet.threedollar.api.user.service.SetupUserServiceTest;
import com.depromeet.threedollar.api.user.service.medal.dto.request.ChangeRepresentativeMedalRequest;
import com.depromeet.threedollar.api.user.testhelper.assertions.UserAssertionHelper;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.rds.user.domain.medal.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
    class ActivateUserMedalTest {

        @Test
        void 장착중인_대표_칭호를_변경한다() {
            // given
            Medal medal = MedalCreator.create("붕친맨");
            medalRepository.save(medal);

            UserMedal userMedal = UserMedalCreator.createInActive(medal, user);
            userMedalRepository.save(userMedal);

            ChangeRepresentativeMedalRequest request = ChangeRepresentativeMedalRequest.testInstance(medal.getId());

            // when
            userMedalService.updateRepresentativeMedal(request, userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertAll(
                () -> assertThat(userMedals).hasSize(1),
                () -> UserAssertionHelper.assertUserMedal(userMedals.get(0), user.getId(), medal.getId(), UserMedalStatus.ACTIVE)
            );
        }

        @Test
        void 대표_칭호_변경시_보유하지_않은_칭호를_장착하려하면_NotFound_에러가_발생한다() {
            // given
            Long notFoundMedalId = -1L;
            ChangeRepresentativeMedalRequest request = ChangeRepresentativeMedalRequest.testInstance(notFoundMedalId);

            // when & then
            assertThatThrownBy(() -> userMedalService.updateRepresentativeMedal(request, userId)).isInstanceOfAny(NotFoundException.class);
        }

    }

}
