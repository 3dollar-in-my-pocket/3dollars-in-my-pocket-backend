package com.depromeet.threedollar.domain.rds.user.domain.user;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@TestFixture
public class UserCreator {

    @Builder
    public static User create(
        @NotNull String socialId,
        @NotNull UserSocialType socialType,
        @NotNull String name
    ) {
        return User.builder()
            .socialId(socialId)
            .socialType(socialType)
            .name(name)
            .build();
    }

}
