package com.depromeet.threedollar.domain.user.domain.store

import com.depromeet.threedollar.domain.user.domain.ObjectMother

@ObjectMother
object StorePromotionCreator {

    @JvmStatic
    fun create(
        introduction: String,
        iconUrl: String,
        isDisplayOnMarker: Boolean,
        isDisplayOnTheDetail: Boolean
    ): StorePromotion {
        return StorePromotion.builder()
            .introduction(introduction)
            .iconUrl(iconUrl)
            .isDisplayOnMarker(isDisplayOnMarker)
            .isDisplayOnTheDetail(isDisplayOnTheDetail)
            .build()
    }

}
