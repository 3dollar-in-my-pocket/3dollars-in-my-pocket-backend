package com.depromeet.threedollar.domain.rds.user.domain.medal;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture;
import com.depromeet.threedollar.domain.rds.user.domain.user.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@TestFixture
public class UserMedalCreator {

    @Builder(builderMethodName = "active")
    public static UserMedal createActive(
        @NotNull Medal medal,
        @NotNull User user
    ) {
        return UserMedal.builder()
            .medal(medal)
            .user(user)
            .status(UserMedalStatus.ACTIVE)
            .build();
    }

    @Builder(builderMethodName = "inActive")
    public static UserMedal createInActive(
        @NotNull Medal medal,
        @NotNull User user
    ) {
        return UserMedal.builder()
            .medal(medal)
            .user(user)
            .status(UserMedalStatus.IN_ACTIVE)
            .build();
    }

}
