package com.depromeet.threedollar.domain.user.domain.store;

import com.depromeet.threedollar.common.docs.ObjectMother;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ObjectMother
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StorePromotionCreator {

    public static StorePromotion create(String introduction, String iconUrl, boolean isDisplayOnMarker, boolean isDisplayOnTheDetail) {
        return StorePromotion.builder()
            .introduction(introduction)
            .iconUrl(iconUrl)
            .isDisplayOnMarker(isDisplayOnMarker)
            .isDisplayOnTheDetail(isDisplayOnTheDetail)
            .build();
    }

}
