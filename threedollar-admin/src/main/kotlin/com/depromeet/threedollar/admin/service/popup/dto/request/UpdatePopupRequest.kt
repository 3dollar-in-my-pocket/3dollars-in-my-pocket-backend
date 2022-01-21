package com.depromeet.threedollar.admin.service.popup.dto.request

import com.depromeet.threedollar.domain.user.domain.popup.PopupPlatformType
import com.depromeet.threedollar.domain.user.domain.popup.PopupPositionType
import java.time.LocalDateTime

data class UpdatePopupRequest(
    val positionType: PopupPositionType,
    val platformType: PopupPlatformType,
    val title: String?,
    val subTitle: String?,
    val imageUrl: String,
    val linkUrl: String?,
    val bgColor: String?,
    val fontColor: String?,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime
)
