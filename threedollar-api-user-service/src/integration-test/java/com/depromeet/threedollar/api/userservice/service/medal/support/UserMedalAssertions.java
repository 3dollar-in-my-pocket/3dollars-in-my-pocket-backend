package com.depromeet.threedollar.api.userservice.service.medal.support;

import com.depromeet.threedollar.domain.rds.domain.TestAssertions;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedal;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedalStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestAssertions
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
