package com.depromeet.threedollar.domain.rds.domain.userservice.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.depromeet.threedollar.domain.rds.domain.userservice.medal.Medal;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalAcquisitionConditionType;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalCreator;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedal;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedalStatus;

class UserTest {

    @Test
    void 유저가_메달을_획득한다() {
        // given
        String medalName = "리뷰왕";
        String description = "리뷰 5번 작성시 획득하는 메달";
        String activationIconUrl = "https://active-icon.png";
        String disabledIconUrl = "https://disable-icon.png";
        MedalAcquisitionConditionType conditionType = MedalAcquisitionConditionType.ADD_REVIEW;
        int count = 5;

        User user = UserCreator.create("social-id", UserSocialType.KAKAO, "닉네임");
        Medal medal = MedalCreator.create(medalName, description, activationIconUrl, disabledIconUrl, conditionType, count);

        // when
        user.addMedals(List.of(medal));

        // then
        assertAll(
            () -> assertThat(user.getUserMedals()).hasSize(1),
            () -> assertUserMedal(user.getUserMedals().get(0), medalName, activationIconUrl, conditionType, count)
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
    void 유저가_메달을_획득하면_비활성화_상태로_추가된다() {
        // given
        User user = UserCreator.create("social-id", UserSocialType.KAKAO, "닉네임");
        Medal medalA = MedalCreator.create("메달 A");

        // when
        user.addMedals(List.of(medalA));

        // then
        assertAll(
            () -> assertThat(user.getUserMedals()).hasSize(1),
            () -> assertThat(user.getUserMedals().get(0).getStatus()).isEqualTo(UserMedalStatus.IN_ACTIVE)
        );
    }

    @Test
    void 유저가_장착한_메달을_조회할때_장착한_메달이_없는경우_null_을_반환한다() {
        // given
        User user = UserCreator.create("social-id", UserSocialType.KAKAO, "닉네임");
        Medal medalA = MedalCreator.create("메달 A");
        user.addMedals(List.of(medalA));

        // when
        UserMedal userMedal = user.getActivatedMedal();

        // then
        assertThat(userMedal).isNull();
    }

}
