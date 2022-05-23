package com.depromeet.threedollar.domain.rds.vendor.domain.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.jetbrains.annotations.NotNull;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Embeddable
public class UserSocialInfo {

    @Column(nullable = false, length = 200)
    private String socialId;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private UserSocialType socialType;

    private UserSocialInfo(@NotNull String socialId, @NotNull UserSocialType socialType) {
        this.socialId = socialId;
        this.socialType = socialType;
    }

    public static UserSocialInfo of(@NotNull String socialId, @NotNull UserSocialType socialType) {
        return new UserSocialInfo(socialId, socialType);
    }

}
