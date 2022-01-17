package com.depromeet.threedollar.domain.user.domain.store;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StorePromotionCreator {

    public static StorePromotion create(String title, String introduction, String imageUrl) {
        return StorePromotion.builder()
            .title(title)
            .introduction(introduction)
            .imageUrl(imageUrl)
            .build();
    }

}
