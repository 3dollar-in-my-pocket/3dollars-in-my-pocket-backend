package com.depromeet.threedollar.domain.user.domain.store;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class StorePromotionOption {

    @Column(nullable = false)
    private boolean isDisplayOnMarker;

    @Column(nullable = false)
    private boolean isDisplayOnTheDetail;

    public static StorePromotionOption of(boolean isDisplayOnMarker, boolean isDisplayOnTheDetail) {
        return new StorePromotionOption(isDisplayOnMarker, isDisplayOnTheDetail);
    }

}
