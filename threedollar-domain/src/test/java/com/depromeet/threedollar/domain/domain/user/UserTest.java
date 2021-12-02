package com.depromeet.threedollar.domain.domain.user;

import com.depromeet.threedollar.domain.domain.medal.*;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserTest {

    @AutoSource
    @ParameterizedTest
    void 유저에게_보유한_메달을_추가한다(String medalName, String medalIconUrl, MedalAcquisitionConditionType conditionType, int count) {
        // given
        User user = UserCreator.create("social-id", UserSocialType.KAKAO, "닉네임");
        Medal medal = MedalCreator.create(medalName, medalIconUrl, conditionType, count);

        // when
        user.addMedals(List.of(medal));

        // then
        assertAll(
            () -> assertThat(user.getUserMedals()).hasSize(1),
            () -> assertUserMedal(user.getUserMedals().get(0), medalName, medalIconUrl, conditionType, count)
        );
    }

    private void assertUserMedal(UserMedal userMedal, String name, String iconUrl, MedalAcquisitionConditionType conditionType, int count) {
        assertAll(
            () -> assertThat(userMedal.getMedal().getName()).isEqualTo(name),
            () -> assertThat(userMedal.getMedal().getIconUrl()).isEqualTo(iconUrl),
            () -> assertThat(userMedal.getMedal().getAcquisitionCondition().getConditionType()).isEqualTo(conditionType),
            () -> assertThat(userMedal.getMedal().getAcquisitionCondition().getCount()).isEqualTo(count)
        );
    }

    @Test
    void 유저에게_메달이_추가되면_기본적으로_비활성화_상태이다() {
        // given
        User user = UserCreator.create("social-id", UserSocialType.KAKAO, "닉네임");
        Medal medalA = MedalCreator.create("메달 A", "iconUrl", MedalAcquisitionConditionType.ADD_STORE, 3);

        // when
        user.addMedals(List.of(medalA));

        // then
        assertAll(
            () -> assertThat(user.getUserMedals()).hasSize(1),
            () -> assertThat(user.getUserMedals().get(0).getStatus()).isEqualTo(UserMedalStatus.IN_ACTIVE)
        );
    }

    @Test
    void 유저가_장착한_대표_메달을_조회한다() {
        // given
        User user = UserCreator.create("social-id", UserSocialType.KAKAO, "닉네임");
        Medal medalA = MedalCreator.create("메달 A", "iconUrl", MedalAcquisitionConditionType.ADD_STORE, 3);
        user.addMedals(List.of(medalA));

        // when
        UserMedal userMedal = user.getActivatedMedal();

        // then
        assertThat(userMedal).isNull();
    }

    @NullSource
    @ParameterizedTest
    void null을_넘기면_유저의_대표_메달을_비활성화시킨다(Long userMedalId) {
        // given
        User user = UserCreator.create("social-id", UserSocialType.KAKAO, "닉네임");
        Medal medalA = MedalCreator.create("메달 A", "iconUrl", MedalAcquisitionConditionType.ADD_STORE, 3);
        Medal medalB = MedalCreator.create("메달 B", "iconUrl", MedalAcquisitionConditionType.VISIT_STORE, 2);
        user.addMedals(List.of(medalA, medalB));

        // when
        user.updateActivatedMedal(userMedalId);

        // then
        assertThat(user.getUserMedals()).extracting(UserMedal::getStatus).containsOnly(UserMedalStatus.IN_ACTIVE);
    }

}
