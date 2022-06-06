package com.depromeet.threedollar.api.adminservice.service.userservice.advertisement.dto.request

import java.time.LocalDateTime
import javax.validation.constraints.Size
import org.hibernate.validator.constraints.URL
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.Advertisement
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementDetail
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPlatformType
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPositionType

data class AddAdvertisementRequest(
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
) {

    fun toEntity(): Advertisement {
        val detail = AdvertisementDetail.of(title, subTitle, imageUrl, linkUrl, bgColor, fontColor)
        return Advertisement.newInstance(position, platform, startDateTime, endDateTime, detail)
    }

}
