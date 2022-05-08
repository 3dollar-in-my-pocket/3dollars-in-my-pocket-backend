package com.depromeet.threedollar.domain.rds.user.domain.user;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@TestFixture
public class WithdrawalUserCreator {

    @Builder
    public static WithdrawalUser create(
        @NotNull String socialId,
        @NotNull UserSocialType socialType,
        @NotNull Long userId,
        @NotNull String name
    ) {
        return WithdrawalUser.builder()
            .userId(userId)
            .socialInfo(SocialInfo.of(socialId, socialType))
            .name(name)
            .build();
    }

}
