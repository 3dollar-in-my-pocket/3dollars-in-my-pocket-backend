package com.depromeet.threedollar.domain.boss.domain.account;

import com.depromeet.threedollar.domain.common.domain.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BossAccount extends AuditingTimeEntity {

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
