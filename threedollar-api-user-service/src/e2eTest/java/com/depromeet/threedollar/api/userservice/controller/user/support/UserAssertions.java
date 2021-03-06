package com.depromeet.threedollar.api.userservice.controller.user.support;

import com.depromeet.threedollar.api.userservice.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.domain.rds.domain.TestAssertions;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserSocialType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestAssertions
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
