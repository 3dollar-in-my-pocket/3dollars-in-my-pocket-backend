package com.depromeet.threedollar.domain.rds.user.domain.advertisement

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture
import java.time.LocalDateTime

@TestFixture
object AdvertisementCreator {

    @JvmStatic
    fun create(advertisementPositionType: AdvertisementPositionType,
               platformType: AdvertisementPlatformType,
               title: String?,
               subTitle: String?,
               imageUrl: String,
               linkUrl: String?,
               bgColor: String?,
               fontColor: String?,
               startDateTime: LocalDateTime,
               endDateTime: LocalDateTime
    ): Advertisement {
        return Advertisement.builder()
            .title(title)
            .subTitle(subTitle)
            .positionType(advertisementPositionType)
            .platformType(platformType)
            .imageUrl(imageUrl)
            .linkUrl(linkUrl)
            .bgColor(bgColor)
            .fontColor(fontColor)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .build()
    }

}
