package com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement

import java.time.LocalDateTime
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.domain.rds.domain.TestFixture

@TestFixture
object AdvertisementCreator {

    @JvmStatic
    fun create(
        applicationType: ApplicationType,
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
            .applicationType(applicationType)
            .positionType(positionType)
            .platformType(platformType)
            .detail(
                AdvertisementDetail.builder()
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
