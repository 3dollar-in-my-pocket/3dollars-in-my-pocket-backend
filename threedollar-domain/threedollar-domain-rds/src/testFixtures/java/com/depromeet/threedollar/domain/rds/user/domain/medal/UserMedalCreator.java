package com.depromeet.threedollar.domain.rds.user.domain.medal;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture;
import com.depromeet.threedollar.domain.rds.user.domain.user.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@TestFixture
public class UserMedalCreator {

    @Builder
    public static UserMedal create(
        @NotNull Medal medal,
        @NotNull User user,
        @Nullable UserMedalStatus status
    ) {
        return UserMedal.builder()
            .medal(medal)
            .user(user)
            .status(Optional.ofNullable(status).orElse(UserMedalStatus.ACTIVE))
            .build();
    }

}
