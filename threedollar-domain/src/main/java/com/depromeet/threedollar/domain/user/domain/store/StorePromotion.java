package com.depromeet.threedollar.domain.user.domain.store;

import com.depromeet.threedollar.domain.common.domain.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class StorePromotion extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String introduction;

    @Column(nullable = false, length = 2048)
    private String iconUrl;

    @Embedded
    private StorePromotionOption option;

    @Builder(access = AccessLevel.PACKAGE)
    private StorePromotion(String introduction, String iconUrl, boolean isDisplayOnMarker, boolean isDisplayOnTheDetail) {
        this.introduction = introduction;
        this.iconUrl = iconUrl;
        this.option = StorePromotionOption.of(isDisplayOnMarker, isDisplayOnTheDetail);
    }

}
