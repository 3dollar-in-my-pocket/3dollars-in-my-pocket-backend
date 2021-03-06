package com.depromeet.threedollar.api.adminservice.service.commonservice.advertisement.dto.request

import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.Advertisement
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementDetail
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPlatformType
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPositionType
import org.hibernate.validator.constraints.URL
import java.time.LocalDateTime
import javax.validation.constraints.Size

data class AddAdvertisementRequest(
    val applicationType: ApplicationType,

    val position: AdvertisementPositionType,

    val platform: AdvertisementPlatformType,

    @field:Size(max = 50, message = "{advertisement.title.size}")
    val title: String?,

    @field:Size(max = 100, message = "{advertisement.subTitle.size}")
    val subTitle: String?,

    @field:URL(message = "{advertisement.imageUrl.url}")
    @field:Size(max = 300, message = "{advertisement.imageUrl.size}")
    val imageUrl: String,

    @field:URL(message = "{advertisement.linkUrl.url}")
    @field:Size(max = 300, message = "{advertisement.linkUrl.size}")
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
        return Advertisement.newInstance(applicationType, position, platform, startDateTime, endDateTime, detail)
    }

}
