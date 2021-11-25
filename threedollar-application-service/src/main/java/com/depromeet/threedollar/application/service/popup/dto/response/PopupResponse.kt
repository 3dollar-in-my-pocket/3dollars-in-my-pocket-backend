package com.depromeet.threedollar.application.service.popup.dto.response

import com.depromeet.threedollar.application.common.dto.AuditingTimeResponse
import com.depromeet.threedollar.domain.domain.popup.Popup
import java.time.LocalDateTime

data class PopupResponse(
    val imageUrl: String,
    val linkUrl: String,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime
) : AuditingTimeResponse() {

    companion object {
        fun of(popup: Popup): PopupResponse {
            val response = PopupResponse(popup.imageUrl, popup.linkUrl, popup.startDateTime, popup.endDateTime)
            response.setBaseTime(popup)
            return response
        }
    }

}
