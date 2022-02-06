package com.depromeet.threedollar.admin.service.advertisement.dto.request

import com.depromeet.threedollar.domain.user.domain.advertisement.AdvertisementPlatformType
import com.depromeet.threedollar.domain.user.domain.advertisement.AdvertisementPositionType
import java.time.LocalDateTime

data class UpdateAdvertisementRequest(
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
)
