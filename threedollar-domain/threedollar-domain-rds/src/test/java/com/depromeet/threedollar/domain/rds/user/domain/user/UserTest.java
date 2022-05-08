package com.depromeet.threedollar.domain.rds.user.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.depromeet.threedollar.domain.rds.user.domain.medal.Medal;
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalAcquisitionConditionType;
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalCreator;
import com.depromeet.threedollar.domain.rds.user.domain.medal.UserMedal;
import com.depromeet.threedollar.domain.rds.user.domain.medal.UserMedalStatus;

class UserTest {

    @Test
    void 유저가_메달을_획득한다() {
        // given
        String medalName = "리뷰왕";
        String description = "리뷰 5번 작성시 획득하는 메달";
        String activationIconUrl = "activationIconUrl";
        String disabledIconUrl = "disabledIconUrl";
        MedalAcquisitionConditionType conditionType = MedalAcquisitionConditionType.ADD_REVIEW;
        int count = 5;

        User user = UserCreator.builder()
            .socialId("social-id")
            .socialType(UserSocialType.KAKAO)
            .name("닉네임")
            .build();

        Medal medal = MedalCreator.builder()
            .name(medalName)
            .acquisitionDescription(description)
            .activationIconUrl(activationIconUrl)
            .disableIconUrl(disabledIconUrl)
            .conditionType(conditionType)
            .conditionCount(count)
            .build();

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
    void 유저가_메달을_획득하면_비활성화_상태로_보관된다() {
        // given
        User user = UserCreator.builder()
            .socialId("social-id")
            .socialType(UserSocialType.KAKAO)
            .name("닉네임")
            .build();

        Medal medalA = MedalCreator.builder()
            .name("메달 A")
            .build();

        // when
        user.addMedals(List.of(medalA));

        // then
        assertAll(
            () -> assertThat(user.getUserMedals()).hasSize(1),
            () -> assertThat(user.getUserMedals().get(0).getStatus()).isEqualTo(UserMedalStatus.IN_ACTIVE)
        );
    }

    @Test
    void 유저가_장착한_메달을_조회할때_장착한_메달이_없는경우_예외가_아닌_null_을_반환한다() {
        // given
        User user = UserCreator.builder()
            .socialId("social-id")
            .socialType(UserSocialType.KAKAO)
            .name("닉네임")
            .build();

        Medal medalA = MedalCreator.builder()
            .name("메달 A")
            .build();

        user.addMedals(List.of(medalA));

        // when
        UserMedal userMedal = user.getActivatedMedal();

        // then
        assertThat(userMedal).isNull();
    }

}
