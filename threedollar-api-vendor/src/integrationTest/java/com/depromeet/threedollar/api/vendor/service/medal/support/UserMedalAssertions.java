package com.depromeet.threedollar.api.vendor.service.medal.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.depromeet.threedollar.domain.rds.vendor.domain.TestHelper;
import com.depromeet.threedollar.domain.rds.vendor.domain.medal.UserMedal;
import com.depromeet.threedollar.domain.rds.vendor.domain.medal.UserMedalStatus;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@TestHelper
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMedalAssertions {

    public static void assertUserMedal(UserMedal userMedal, Long userId, Long medalId, UserMedalStatus status) {
        assertAll(
            () -> assertThat(userMedal.getUser().getId()).isEqualTo(userId),
            () -> assertThat(userMedal.getStatus()).isEqualTo(status),
            () -> assertThat(userMedal.getMedal().getId()).isEqualTo(medalId)
        );
    }

}
