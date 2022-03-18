package com.depromeet.threedollar.api.admin.service.user.advertisement.dto.request

import com.depromeet.threedollar.domain.rds.user.domain.advertisement.Advertisement
import com.depromeet.threedollar.domain.rds.user.domain.advertisement.AdvertisementDetail
import com.depromeet.threedollar.domain.rds.user.domain.advertisement.AdvertisementPlatformType
import com.depromeet.threedollar.domain.rds.user.domain.advertisement.AdvertisementPositionType
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.URL
import java.time.LocalDateTime

data class AddAdvertisementRequest(
    val position: AdvertisementPositionType,

    val platform: AdvertisementPlatformType,

    @field:Length(max = 50, message = "{advertisement.title.length}")
    val title: String?,

    @field:Length(max = 100, message = "{advertisement.subTitle.length}")
    val subTitle: String?,

    @field:URL(message = "{advertisement.imageUrl.url}")
    @field:Length(max = 2048, message = "{advertisement.imageUrl.length}")
    val imageUrl: String,

    @field:URL(message = "{advertisement.linkUrl.url}")
    @field:Length(max = 2048, message = "{advertisement.linkUrl.length}")
    val linkUrl: String?,

    @field:Length(min = 7, max = 7, message = "{advertisement.bgColor.length}")
    val bgColor: String?,

    @field:Length(min = 7, max = 7, message = "{advertisement.fontColor.length}")
    val fontColor: String?,

    val startDateTime: LocalDateTime,

    val endDateTime: LocalDateTime
) {

    fun toEntity(): Advertisement {
        val detail = AdvertisementDetail.of(title, subTitle, imageUrl, linkUrl, bgColor, fontColor)
        return Advertisement.newInstance(position, platform, startDateTime, endDateTime, detail)
    }

}
