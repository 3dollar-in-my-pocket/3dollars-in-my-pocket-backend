package com.depromeet.threedollar.api.userservice.service.medal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.depromeet.threedollar.api.userservice.service.SetupUserServiceTest;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.Medal;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalAcquisitionConditionType;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalCreator;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedal;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedalStatus;

@SpringBootTest
class AddUserMedalServiceTest extends SetupUserServiceTest {

    @Autowired
    private AddUserMedalService addUserMedalService;

    @AfterEach
    void cleanUp() {
        super.cleanup();
    }

    @Test
    void 기본_메달을_유저에게_부여하면_대표_메달로_등록되고_보유중인_메달에_추가된다() {
        // given
        Medal medal = MedalCreator.create("기본 메달", MedalAcquisitionConditionType.NO_CONDITION, 0);
        medalRepository.save(medal);

        // when
        addUserMedalService.addAndActivateDefaultMedals(userId);

        // then
        List<UserMedal> userMedals = userMedalRepository.findAll();
        assertAll(
            () -> assertThat(userMedals).hasSize(1),
            () -> assertThat(userMedals.get(0).getMedal().getId()).isEqualTo(medal.getId()),
            () -> assertThat(userMedals.get(0).getUser().getId()).isEqualTo(userId)
        );
    }

    @Test
    void 기본_메달을_유저에게_부여하면_대표_메달로_등록된다() {
        // given
        Medal medal = MedalCreator.create("기본 메달", MedalAcquisitionConditionType.NO_CONDITION, 0);
        medalRepository.save(medal);

        // when
        addUserMedalService.addAndActivateDefaultMedals(userId);

        // then
        List<UserMedal> userMedals = userMedalRepository.findAll();
        assertAll(
            () -> assertThat(userMedals).hasSize(1),
            () -> assertThat(userMedals.get(0).getStatus()).isEqualTo(UserMedalStatus.ACTIVE)
        );
    }

    @Test
    void 기본_메달을_둘_이상_획득하면_획득한_첫번째_메달이_대표_메달로_등록된다() {
        // given
        Medal medalFirst = MedalCreator.create("첫번째 메달", MedalAcquisitionConditionType.NO_CONDITION, 0);
        Medal medalSecond = MedalCreator.create("두번째 메달", MedalAcquisitionConditionType.NO_CONDITION, 0);
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