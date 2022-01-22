package com.depromeet.threedollar.domain.user.domain.popup

import com.depromeet.threedollar.domain.user.domain.TestFixture
import java.time.LocalDateTime

@TestFixture
object PopupCreator {

    @JvmStatic
    fun create(popupPositionType: PopupPositionType,
               platformType: PopupPlatformType,
               title: String?,
               subTitle: String?,
               imageUrl: String,
               linkUrl: String?,
               bgColor: String?,
               fontColor: String?,
               startDateTime: LocalDateTime,
               endDateTime: LocalDateTime
    ): Popup {
        return Popup.builder()
            .title(title)
            .subTitle(subTitle)
            .positionType(popupPositionType)
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
