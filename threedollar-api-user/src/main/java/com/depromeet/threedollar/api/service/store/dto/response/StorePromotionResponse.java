package com.depromeet.threedollar.api.service.store.dto.response;

import com.depromeet.threedollar.domain.user.domain.store.StorePromotion;
import lombok.*;
import org.jetbrains.annotations.Nullable;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StorePromotionResponse {

    private String title;

    private String introduction;

    @Nullable
    private String imageUrl;

    public static StorePromotionResponse of(@Nullable StorePromotion promotion) {
        if (promotion == null) {
            return null;
        }
        return new StorePromotionResponse(promotion.getTitle(), promotion.getIntroduction(), promotion.getImageUrl());
    }

}
