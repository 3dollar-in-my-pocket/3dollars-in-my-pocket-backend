package com.depromeet.threedollar.domain.rds.vendor.domain.advertisement

import java.time.LocalDateTime
import com.depromeet.threedollar.domain.rds.vendor.domain.TestFixture

@TestFixture
object AdvertisementCreator {

    @JvmStatic
    fun create(
        positionType: AdvertisementPositionType,
        platformType: AdvertisementPlatformType,
        title: String?,
        subTitle: String?,
        imageUrl: String,
        linkUrl: String?,
        bgColor: String?,
        fontColor: String?,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
    ): Advertisement {
        return Advertisement.builder()
            .positionType(positionType)
            .platformType(platformType)
            .detail(AdvertisementDetail.builder()
                .title(title)
                .subTitle(subTitle)
                .imageUrl(imageUrl)
                .linkUrl(linkUrl)
                .bgColor(bgColor)
                .fontColor(fontColor)
                .build()
            )
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .build()
    }

}
