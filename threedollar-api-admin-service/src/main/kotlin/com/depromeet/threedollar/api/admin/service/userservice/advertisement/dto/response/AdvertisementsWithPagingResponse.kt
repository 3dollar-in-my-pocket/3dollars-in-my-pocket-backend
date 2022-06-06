package com.depromeet.threedollar.api.admin.service.userservice.advertisement.dto.response

import java.time.LocalDateTime
import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.Advertisement
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPlatformType
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPositionType

data class AdvertisementsWithPagingResponse(
    val contents: List<AdvertisementResponse>,
    val totalCounts: Long,
) {

    companion object {
        fun of(advertisements: List<Advertisement>, totalCounts: Long): AdvertisementsWithPagingResponse {
            return AdvertisementsWithPagingResponse(
                contents = advertisements.map { advertisement -> AdvertisementResponse.of(advertisement) },
                totalCounts = totalCounts
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
    val endDateTime: LocalDateTime,
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
            response.setAuditingTimeByEntity(advertisement)
            return response
        }
    }

}
