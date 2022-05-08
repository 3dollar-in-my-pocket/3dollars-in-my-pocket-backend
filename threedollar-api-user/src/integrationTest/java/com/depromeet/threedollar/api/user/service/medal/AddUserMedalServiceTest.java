package com.depromeet.threedollar.api.user.service.medal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.depromeet.threedollar.api.user.service.SetupUserServiceTest;
import com.depromeet.threedollar.domain.rds.user.domain.medal.Medal;
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalAcquisitionConditionType;
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalCreator;
import com.depromeet.threedollar.domain.rds.user.domain.medal.UserMedal;
import com.depromeet.threedollar.domain.rds.user.domain.medal.UserMedalStatus;

@SpringBootTest
class AddUserMedalServiceTest extends SetupUserServiceTest {

    @Autowired
    private AddUserMedalService addUserMedalService;

    @AfterEach
    void cleanUp() {
        super.cleanup();
    }

    @Test
    void 기본_메달을_유저에게_부여하면_새로운_유저_메달이_추가되고_활성화된다() {
        // given
        Medal medal = MedalCreator.builder()
            .name("기본 메달")
            .conditionType(MedalAcquisitionConditionType.NO_CONDITION)
            .conditionCount(0)
            .build();
        medalRepository.save(medal);

        // when
        addUserMedalService.addAndActivateDefaultMedals(userId);

        // then
        List<UserMedal> userMedals = userMedalRepository.findAll();
        assertAll(
            () -> assertThat(userMedals).hasSize(1),
            () -> assertThat(userMedals.get(0).getMedal().getId()).isEqualTo(medal.getId()),
            () -> assertThat(userMedals.get(0).getStatus()).isEqualTo(UserMedalStatus.ACTIVE),
            () -> assertThat(userMedals.get(0).getUser().getId()).isEqualTo(userId)
        );
    }

    @Test
    void 여러_기본_메달이_있는경우_첫번째_메달만_활성화된다() {
        // given
        Medal medalFirst = MedalCreator.builder()
            .name("첫번째 메달")
            .conditionType(MedalAcquisitionConditionType.NO_CONDITION)
            .conditionCount(0)
            .build();
        Medal medalSecond = MedalCreator.builder()
            .name("두번째 메달")
            .conditionType(MedalAcquisitionConditionType.NO_CONDITION)
            .conditionCount(0)
            .build();
        medalRepository.saveAll(List.of(medalFirst, medalSecond));

        // when
        addUserMedalService.addAndActivateDefaultMedals(userId);

        // then
        List<UserMedal> userMedals = userMedalRepository.findAll();
        assertAll(
            () -> assertThat(userMedals).hasSize(2),
            () -> assertThat(userMedals.get(0).getMedal().getId()).isEqualTo(medalFirst.getId()),
            () -> assertThat(userMedals.get(0).getStatus()).isEqualTo(UserMedalStatus.ACTIVE),

            () -> assertThat(userMedals.get(1).getMedal().getId()).isEqualTo(medalSecond.getId()),
            () -> assertThat(userMedals.get(1).getStatus()).isEqualTo(UserMedalStatus.IN_ACTIVE)
        );
    }

}
