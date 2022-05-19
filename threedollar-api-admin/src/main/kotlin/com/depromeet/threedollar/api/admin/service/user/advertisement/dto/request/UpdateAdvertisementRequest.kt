package com.depromeet.threedollar.api.admin.service.user.advertisement.dto.request

import java.time.LocalDateTime
import javax.validation.constraints.Size
import org.hibernate.validator.constraints.URL
import com.depromeet.threedollar.domain.rds.user.domain.advertisement.AdvertisementPlatformType
import com.depromeet.threedollar.domain.rds.user.domain.advertisement.AdvertisementPositionType

data class UpdateAdvertisementRequest(
    val position: AdvertisementPositionType,

    val platform: AdvertisementPlatformType,

    @field:Size(max = 50, message = "{advertisement.title.size}")
    val title: String?,

    @field:Size(max = 100, message = "{advertisement.subTitle.size}")
    val subTitle: String?,

    @field:URL(message = "{advertisement.imageUrl.url}")
    @field:Size(max = 2048, message = "{advertisement.imageUrl.size}")
    val imageUrl: String,

    @field:URL(message = "{advertisement.linkUrl.url}")
    @field:Size(max = 2048, message = "{advertisement.linkUrl.size}")
    val linkUrl: String?,

    @field:Size(min = 7, max = 7, message = "{advertisement.bgColor.size}")
    val bgColor: String?,

    @field:Size(min = 7, max = 7, message = "{advertisement.fontColor.size}")
    val fontColor: String?,

    val startDateTime: LocalDateTime,

    val endDateTime: LocalDateTime,
)
