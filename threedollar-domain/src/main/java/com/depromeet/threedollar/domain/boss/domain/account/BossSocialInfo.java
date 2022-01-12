package com.depromeet.threedollar.domain.boss.domain.account;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Embeddable
public class BossSocialInfo {

    @Column(nullable = false, length = 200)
    private String socialId;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private BossAccountSocialType socialType;

    private BossSocialInfo(String socialId, BossAccountSocialType socialType) {
        this.socialId = socialId;
        this.socialType = socialType;
    }

    public static BossSocialInfo of(String socialId, BossAccountSocialType socialType) {
        return new BossSocialInfo(socialId, socialType);
    }

}
