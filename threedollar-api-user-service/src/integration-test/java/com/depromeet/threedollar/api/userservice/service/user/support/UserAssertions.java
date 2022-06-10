package com.depromeet.threedollar.api.userservice.service.user.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.depromeet.threedollar.domain.rds.domain.TestHelper;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserSocialInfo;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserSocialType;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.WithdrawalUser;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@TestHelper
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserAssertions {

    public static void assertUser(User user, String socialId, UserSocialType socialType, String name) {
        assertAll(
            () -> assertThat(user.getSocialInfo()).isEqualTo(UserSocialInfo.of(socialId, socialType)),
            () -> assertThat(user.getName()).isEqualTo(name)
        );
    }

    public static void assertWithdrawalUser(WithdrawalUser withdrawalUser, User user) {
        assertAll(
            () -> assertThat(withdrawalUser.getUserId()).isEqualTo(user.getId()),
            () -> assertThat(withdrawalUser.getName()).isEqualTo(user.getName()),
            () -> assertThat(withdrawalUser.getSocialInfo()).isEqualTo(user.getSocialInfo()),
            () -> assertThat(withdrawalUser.getUserCreatedAt()).isEqualTo(user.getCreatedAt())
        );
    }

    public static void assertWithdrawalUser(WithdrawalUser withdrawalUser, Long userId, String name, UserSocialInfo socialInfo) {
        assertAll(
            () -> assertThat(withdrawalUser.getUserId()).isEqualTo(userId),
            () -> assertThat(withdrawalUser.getName()).isEqualTo(name),
            () -> assertThat(withdrawalUser.getSocialInfo()).isEqualTo(socialInfo)
        );
    }

}
