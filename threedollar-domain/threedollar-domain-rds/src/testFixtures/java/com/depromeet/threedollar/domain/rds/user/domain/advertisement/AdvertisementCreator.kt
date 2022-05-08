package com.depromeet.threedollar.domain.rds.user.domain.advertisement

import java.time.LocalDateTime
import com.depromeet.threedollar.domain.rds.user.domain.TestFixture

@TestFixture
object AdvertisementCreator {

    @JvmOverloads
    @JvmStatic
    fun create(
        positionType: AdvertisementPositionType,
        platformType: AdvertisementPlatformType,
        imageUrl: String,
        title: String? = null,
        subTitle: String? = null,
        linkUrl: String? = null,
        bgColor: String? = null,
        fontColor: String? = null,
        startDateTime: LocalDateTime = LocalDateTime.of(2022, 1, 1, 0, 0),
        endDateTime: LocalDateTime = LocalDateTime.of(2022, 1, 10, 0, 0)
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
