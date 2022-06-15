package com.depromeet.threedollar.api.userservice.service.medal;

import static com.depromeet.threedollar.api.userservice.service.medal.support.UserMedalAssertions.assertUserMedal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.depromeet.threedollar.api.userservice.SetupUserServiceTest;
import com.depromeet.threedollar.api.userservice.service.medal.dto.request.ChangeRepresentativeMedalRequest;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.Medal;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalCreator;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedal;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedalCreator;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedalRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedalStatus;

class UserMedalServiceTest extends SetupUserServiceTest {

    @Autowired
    private UserMedalService userMedalService;

    @Autowired
    private MedalRepository medalRepository;

    @Autowired
    private UserMedalRepository userMedalRepository;

    @DisplayName("유저의 장착중인 대표 칭호를 변경한다")
    @Nested
    class ActivateUserMedalTest {

        @Test
        void 대표_메달을_변경합니다() {
            // given
            Medal medal = MedalCreator.create("붕친맨");
            medalRepository.save(medal);

            UserMedal userMedal = UserMedalCreator.create(medal, user, UserMedalStatus.IN_ACTIVE);
            userMedalRepository.save(userMedal);

            ChangeRepresentativeMedalRequest request = ChangeRepresentativeMedalRequest.testBuilder()
                .medalId(medal.getId())
                .build();

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
        void 대표_메달을_수정할때_보유하지_않은_메달을_장착하려하면_NOTFOUND_에러가_발생합니다() {
            // given
            Long notFoundMedalId = -1L;

            ChangeRepresentativeMedalRequest request = ChangeRepresentativeMedalRequest.testBuilder()
                .medalId(notFoundMedalId)
                .build();

            // when & then
            assertThatThrownBy(() -> userMedalService.updateRepresentativeMedal(request, userId)).isInstanceOf(NotFoundException.class);
        }

    }

}
