package com.depromeet.threedollar.admin.service.advertisement.dto.response

import com.depromeet.threedollar.application.common.dto.AuditingTimeResponse
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
) : AuditingTimeResponse() {

    companion object {
        fun of(advertisement: Advertisement): AdvertisementResponse {
            val response = AdvertisementResponse(
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
            response.setBaseTime(advertisement)
            return response
        }
    }

}
