package com.depromeet.threedollar.application.service.advertisement.dto.response

import com.depromeet.threedollar.domain.user.domain.advertisement.AdvertisementDetail

data class AdvertisementResponse(
    val title: String?,
    val subTitle: String?,
    val imageUrl: String,
    val linkUrl: String?,
    val bgColor: String?,
    val fontColor: String?
) {

    companion object {
        fun of(advertisement: AdvertisementDetail): AdvertisementResponse {
            return AdvertisementResponse(
                title = advertisement.title,
                subTitle = advertisement.subTitle,
                imageUrl = advertisement.imageUrl,
                linkUrl = advertisement.linkUrl,
                bgColor = advertisement.bgColor,
                fontColor = advertisement.fontColor
            )
        }
    }

}
