package com.depromeet.threedollar.admin.service.user.advertisement.dto.request

import com.depromeet.threedollar.domain.user.domain.advertisement.Advertisement
import com.depromeet.threedollar.domain.user.domain.advertisement.AdvertisementPlatformType
import com.depromeet.threedollar.domain.user.domain.advertisement.AdvertisementPositionType
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.URL
import java.time.LocalDateTime
import javax.validation.constraints.FutureOrPresent

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

    @field:FutureOrPresent(message = "{advertisement.startDateTime.futureOrPresent}")
    val startDateTime: LocalDateTime,

    @field:FutureOrPresent(message = "{advertisement.endDateTime.futureOrPresent}")
    val endDateTime: LocalDateTime
) {

    fun toEntity(): Advertisement {
        return Advertisement.newInstance(position, platform, title, subTitle, imageUrl, linkUrl, bgColor, fontColor, startDateTime, endDateTime)
    }

}
