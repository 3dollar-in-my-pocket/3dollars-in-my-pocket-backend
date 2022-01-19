package com.depromeet.threedollar.admin.service.popup.dto.request

import com.depromeet.threedollar.domain.user.domain.popup.Popup
import com.depromeet.threedollar.domain.user.domain.popup.PopupPlatformType
import com.depromeet.threedollar.domain.user.domain.popup.PopupPositionType
import java.time.LocalDateTime

data class AddPopupRequest(
    val positionType: PopupPositionType,
    val platformType: PopupPlatformType,
    val imageUrl: String,
    val linkUrl: String?,
    val priority: Int,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime
) {

    fun toEntity(): Popup {
        return Popup.newInstance(positionType, platformType, imageUrl, linkUrl, startDateTime, endDateTime, priority)
    }

}
