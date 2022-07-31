package com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement

import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.domain.rds.domain.TestFixture
import java.time.LocalDateTime

@TestFixture
object AdvertisementFixture {

    @JvmStatic
    fun create(
        applicationType: ApplicationType,
        positionType: AdvertisementPositionType,
        platformType: AdvertisementPlatformType,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        title: String? = "붕어빵 팔아요!",
        subTitle: String? = "붕어빵 팔아요 부제",
        imageUrl: String = "https://image-a.png",
        linkUrl: String? = "https://link-a.com",
        bgColor: String? = "#000000",
        fontColor: String? = "#ffffff",
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
