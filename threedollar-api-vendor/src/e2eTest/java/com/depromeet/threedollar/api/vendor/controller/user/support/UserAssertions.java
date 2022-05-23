package com.depromeet.threedollar.api.vendor.controller.user.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.depromeet.threedollar.api.vendor.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.domain.rds.vendor.domain.TestHelper;
import com.depromeet.threedollar.domain.rds.vendor.domain.user.User;
import com.depromeet.threedollar.domain.rds.vendor.domain.user.UserSocialType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@TestHelper
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserAssertions {

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

}
