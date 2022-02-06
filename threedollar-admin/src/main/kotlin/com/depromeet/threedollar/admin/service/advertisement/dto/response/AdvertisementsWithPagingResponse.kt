package com.depromeet.threedollar.admin.service.advertisement.dto.response

import com.depromeet.threedollar.domain.user.domain.advertisement.Advertisement
import com.depromeet.threedollar.domain.user.domain.advertisement.AdvertisementPlatformType
import com.depromeet.threedollar.domain.user.domain.advertisement.AdvertisementPositionType
import java.time.LocalDateTime

data class AdvertisementsWithPagingResponse(
    val contents: List<AdvertisementResponse>,
    val totalCounts: Long
) {

    companion object {
        fun of(advertisements: List<Advertisement>, totalCounts: Long): AdvertisementsWithPagingResponse {
            return AdvertisementsWithPagingResponse(
                advertisements.map { AdvertisementResponse.of(it) },
                totalCounts
            )
        }
    }

}

data class AdvertisementResponse(
    val advertisementId: Long,
    val positionType: AdvertisementPositionType,
    val platformType: AdvertisementPlatformType,
    val title: String?,
    val subTitle: String?,
    val imageUrl: String,
    val linkUrl: String?,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime
) {

    companion object {
        fun of(advertisement: Advertisement): AdvertisementResponse {
            return AdvertisementResponse(
                advertisementId = advertisement.id,
                positionType = advertisement.positionType,
                platformType = advertisement.platformType,
                title = advertisement.detail.title,
                subTitle = advertisement.detail.subTitle,
                imageUrl = advertisement.detail.imageUrl,
                linkUrl = advertisement.detail.linkUrl,
                startDateTime = advertisement.startDateTime,
                endDateTime = advertisement.endDateTime
            )
        }
    }

}
