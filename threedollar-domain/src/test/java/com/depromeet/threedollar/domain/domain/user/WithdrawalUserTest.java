package com.depromeet.threedollar.domain.domain.user;

import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class WithdrawalUserTest {

    @AutoSource
    @ParameterizedTest
    void 기존의_유저를통해_유저_백업정보를_생성한다(String socialId, UserSocialType socialType, String name) {
        // given
        User user = UserCreator.create(socialId, socialType, name);

        // when
        WithdrawalUser withdrawalUser = WithdrawalUser.newInstance(user);

        // then
        assertAll(
            () -> assertThat(withdrawalUser.getUserId()).isEqualTo(user.getId()),
            () -> assertThat(withdrawalUser.getSocialInfo()).isEqualTo(SocialInfo.of(socialId, socialType)),
            () -> assertThat(withdrawalUser.getName()).isEqualTo(user.getName()),
            () -> assertThat(withdrawalUser.getUserCreatedAt()).isEqualTo(user.getCreatedAt())
        );
    }

}
