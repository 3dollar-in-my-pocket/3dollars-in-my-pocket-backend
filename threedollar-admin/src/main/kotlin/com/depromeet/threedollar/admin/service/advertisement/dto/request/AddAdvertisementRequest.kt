package com.depromeet.threedollar.admin.service.advertisement.dto.request

import com.depromeet.threedollar.domain.user.domain.advertisement.Advertisement
import com.depromeet.threedollar.domain.user.domain.advertisement.AdvertisementPlatformType
import com.depromeet.threedollar.domain.user.domain.advertisement.AdvertisementPositionType
import java.time.LocalDateTime

data class AddAdvertisementRequest(
    val positionType: AdvertisementPositionType,
    val platformType: AdvertisementPlatformType,
    val title: String?,
    val subTitle: String?,
    val imageUrl: String,
    val linkUrl: String?,
    val bgColor: String?,
    val fontColor: String?,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime
) {

    fun toEntity(): Advertisement {
        return Advertisement.newInstance(positionType, platformType, title, subTitle, imageUrl, linkUrl, bgColor, fontColor, startDateTime, endDateTime)
    }

}
