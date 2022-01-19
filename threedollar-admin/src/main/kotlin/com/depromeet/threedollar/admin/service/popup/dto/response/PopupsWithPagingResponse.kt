package com.depromeet.threedollar.admin.service.popup.dto.response

import com.depromeet.threedollar.domain.user.domain.popup.Popup
import com.depromeet.threedollar.domain.user.domain.popup.PopupPlatformType
import com.depromeet.threedollar.domain.user.domain.popup.PopupPositionType
import java.time.LocalDateTime

data class PopupsWithPagingResponse(
    val contents: List<PopupResponse>,
    val totalCounts: Long
) {

    companion object {
        fun of(popups: List<Popup>, totalCounts: Long): PopupsWithPagingResponse {
            return PopupsWithPagingResponse(
                popups.map { PopupResponse.of(it) },
                totalCounts
            )
        }
    }

}

data class PopupResponse(
    val popupId: Long,
    val positionType: PopupPositionType,
    val platformType: PopupPlatformType,
    val imageUrl: String,
    val linkUrl: String?,
    val priority: Int,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime
) {

    companion object {
        fun of(popup: Popup): PopupResponse {
            return PopupResponse(
                popupId = popup.id,
                positionType = popup.positionType,
                platformType = popup.platformType,
                imageUrl = popup.imageUrl,
                linkUrl = popup.linkUrl,
                priority = popup.priority,
                startDateTime = popup.startDateTime,
                endDateTime = popup.endDateTime
            )
        }
    }

}
