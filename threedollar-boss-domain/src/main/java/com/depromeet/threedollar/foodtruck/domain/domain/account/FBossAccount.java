package com.depromeet.threedollar.foodtruck.domain.domain.account;

import com.depromeet.threedollar.foodtruck.domain.domain.BaseTimeEntity;
import com.depromeet.threedollar.foodtruck.domain.domain.BusinessNumber;
import com.depromeet.threedollar.foodtruck.domain.domain.PhoneInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FBossAccount extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private FBossSocialInfo socialInfo;

    private String name;

    private BusinessNumber businessNumber;

    @Embedded
    private PhoneInfo phoneInfo;

}
