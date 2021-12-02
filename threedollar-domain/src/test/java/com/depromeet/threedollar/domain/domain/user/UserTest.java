package com.depromeet.threedollar.domain.domain.user;

import com.depromeet.threedollar.domain.domain.medal.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserTest {

    @Test
    void 유저에_메달을_추가한다() {
        // given
        User user = UserCreator.create("social-id", UserSocialType.KAKAO, "닉네임");

        Medal medalA = MedalCreator.create("메달 A", "iconUrl", MedalAcquisitionConditionType.ADD_STORE, 3);
        Medal medalB = MedalCreator.create("메달 B", "iconUrl", MedalAcquisitionConditionType.VISIT_STORE, 2);

        // when
        user.addMedals(List.of(medalA, medalB));

        // then
        assertAll(
            () -> assertThat(user.getUserMedals()).hasSize(2),
            () -> assertThat(user.getUserMedals().get(0).getMedal().getName()).isEqualTo(medalA.getName()),
            () -> assertThat(user.getUserMedals().get(0).getMedal().getIconUrl()).isEqualTo(medalA.getIconUrl()),
            () -> assertThat(user.getUserMedals().get(0).getMedal().getAcquisitionCondition().getConditionType()).isEqualTo(MedalAcquisitionConditionType.ADD_STORE),
            () -> assertThat(user.getUserMedals().get(0).getMedal().getAcquisitionCondition().getCount()).isEqualTo(3),

            () -> assertThat(user.getUserMedals().get(1).getMedal().getName()).isEqualTo(medalB.getName()),
            () -> assertThat(user.getUserMedals().get(1).getMedal().getIconUrl()).isEqualTo(medalB.getIconUrl()),
            () -> assertThat(user.getUserMedals().get(1).getMedal().getAcquisitionCondition().getConditionType()).isEqualTo(MedalAcquisitionConditionType.VISIT_STORE),
            () -> assertThat(user.getUserMedals().get(1).getMedal().getAcquisitionCondition().getCount()).isEqualTo(2)
        );
    }

    @Test
    void 유저에게_메달을_추가하면_기본적으로_비활성화_상태이다() {
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
    void 대표_메달을_조회한다() {
        // given
        User user = UserCreator.create("social-id", UserSocialType.KAKAO, "닉네임");
        Medal medalA = MedalCreator.create("메달 A", "iconUrl", MedalAcquisitionConditionType.ADD_STORE, 3);
        user.addMedals(List.of(medalA));

        // when
        UserMedal userMedal = user.getActivatedMedal();

        // then
        assertThat(userMedal).isNull();
    }

    @Test
    void 유저의_대표_메달을_비활성화시킨다() {
        // given
        User user = UserCreator.create("social-id", UserSocialType.KAKAO, "닉네임");
        Medal medalA = MedalCreator.create("메달 A", "iconUrl", MedalAcquisitionConditionType.ADD_STORE, 3);
        Medal medalB = MedalCreator.create("메달 B", "iconUrl", MedalAcquisitionConditionType.VISIT_STORE, 2);
        user.addMedals(List.of(medalA, medalB));

        user.updateActivatedMedal(null);

        // then
        assertThat(user.getUserMedals()).extracting(UserMedal::getStatus).containsOnly(UserMedalStatus.IN_ACTIVE);
    }

}
