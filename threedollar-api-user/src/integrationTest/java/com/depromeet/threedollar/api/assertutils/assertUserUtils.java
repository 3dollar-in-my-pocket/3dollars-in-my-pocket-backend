package com.depromeet.threedollar.api.assertutils;

import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.domain.user.domain.medal.UserMedal;
import com.depromeet.threedollar.domain.user.domain.medal.UserMedalStatus;
import com.depromeet.threedollar.domain.user.domain.user.SocialInfo;
import com.depromeet.threedollar.domain.user.domain.user.User;
import com.depromeet.threedollar.domain.user.domain.user.UserSocialType;
import com.depromeet.threedollar.domain.user.domain.user.WithdrawalUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public final class assertUserUtils {

    public static void assertWithdrawalUser(WithdrawalUser withdrawalUser, User user) {
        assertAll(
            () -> assertThat(withdrawalUser.getUserId()).isEqualTo(user.getId()),
            () -> assertThat(withdrawalUser.getName()).isEqualTo(user.getName()),
            () -> assertThat(withdrawalUser.getSocialInfo()).isEqualTo(user.getSocialInfo()),
            () -> assertThat(withdrawalUser.getUserCreatedAt()).isEqualTo(user.getCreatedAt())
        );
    }

    public static void assertUser(User user, String socialId, UserSocialType socialType, String name) {
        assertAll(
            () -> assertThat(user.getSocialInfo()).isEqualTo(SocialInfo.of(socialId, socialType)),
            () -> assertThat(user.getName()).isEqualTo(name)
        );
    }

    public static void assertUserInfoResponse(UserInfoResponse response, User user) {
        assertAll(
            () -> assertThat(response.getUserId()).isEqualTo(user.getId()),
            () -> assertThat(response.getName()).isEqualTo(user.getName()),
            () -> assertThat(response.getSocialType()).isEqualTo(user.getSocialType())
        );
    }

    public static void assertUserInfoResponse(UserInfoResponse response, Long userId, String name, UserSocialType socialType) {
        assertAll(
            () -> assertThat(response.getUserId()).isEqualTo(userId),
            () -> assertThat(response.getName()).isEqualTo(name),
            () -> assertThat(response.getSocialType()).isEqualTo(socialType)
        );
    }

    public static void assertUserMedal(UserMedal userMedal, Long userId, Long medalId, UserMedalStatus status) {
        assertAll(
            () -> assertThat(userMedal.getUser().getId()).isEqualTo(userId),
            () -> assertThat(userMedal.getStatus()).isEqualTo(status),
            () -> assertThat(userMedal.getMedal().getId()).isEqualTo(medalId)
        );
    }

}
