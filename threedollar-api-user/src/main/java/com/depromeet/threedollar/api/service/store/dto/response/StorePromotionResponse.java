package com.depromeet.threedollar.api.service.store.dto.response;

import com.depromeet.threedollar.domain.user.domain.store.StorePromotion;
import com.depromeet.threedollar.domain.user.domain.store.StorePromotionOption;
import lombok.*;
import org.jetbrains.annotations.Nullable;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StorePromotionResponse {

    private String introduction;

    private String iconUrl;

    private StorePromotionOptionResponse options;

    public static StorePromotionResponse of(@Nullable StorePromotion promotion) {
        if (promotion == null) {
            return null;
        }
        return new StorePromotionResponse(promotion.getIntroduction(), promotion.getIconUrl(), StorePromotionOptionResponse.of(promotion.getOption()));
    }

    @ToString
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class StorePromotionOptionResponse {

        private boolean marker;

        private boolean detail;

        public static StorePromotionOptionResponse of(StorePromotionOption option) {
            return new StorePromotionOptionResponse(option.isDisplayOnMarker(), option.isDisplayOnTheDetail());
        }

    }

}
