package com.depromeet.threedollar.foodtruck.domain.domain.account;

import com.depromeet.threedollar.foodtruck.domain.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BossAccount extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private BossSocialInfo socialInfo;

    private String name;

    private BossAccount(String socialId, BossAccountSocialType socialType, String name) {
        this.socialInfo = BossSocialInfo.of(socialId, socialType);
        this.name = name;
    }

    public static BossAccount of(String socialId, BossAccountSocialType socialType, String name) {
        return new BossAccount(socialId, socialType, name);
    }

}
