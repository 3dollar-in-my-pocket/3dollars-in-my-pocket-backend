package com.depromeet.threedollar.domain.domain.user;

import com.depromeet.threedollar.domain.domain.medal.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserTest {

    @Test
    void 유저에게_보유한_메달을_추가한다() {
        // given
        String medalName = "리뷰 5번 작성시 획득하는 메달";
        String medalIconUrl = "medalIconUrl";
        MedalAcquisitionConditionType conditionType = MedalAcquisitionConditionType.ADD_REVIEW;
        int count = 5;

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
            () -> assertThat(userMedal.getMedal().getActivationIconUrl()).isEqualTo(iconUrl),
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
    void 유저에게_활성화중인_메달이_없는경우_예외가_아닌_null_을_반환한다() {
        // given
        User user = UserCreator.create("social-id", UserSocialType.KAKAO, "닉네임");
        Medal medalA = MedalCreator.create("메달 A", "iconUrl", MedalAcquisitionConditionType.ADD_STORE, 3);
        user.addMedals(List.of(medalA));

        // when
        UserMedal userMedal = user.getActivatedMedal();

        // then
        assertThat(userMedal).isNull();
    }

}
