package com.depromeet.threedollar.foodtruck.domain.domain.account;

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
public class FBossSocialInfo {

    @Column(nullable = false, length = 200)
    private String socialId;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private FBossAccountSocialType socialType;

    private FBossSocialInfo(String socialId, FBossAccountSocialType socialType) {
        this.socialId = socialId;
        this.socialType = socialType;
    }

}
