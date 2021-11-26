package com.depromeet.threedollar.application.service.popup.dto.response

import com.depromeet.threedollar.application.common.dto.AuditingTimeResponse
import com.depromeet.threedollar.domain.domain.popup.Popup

data class PopupResponse(
    val imageUrl: String,
    val linkUrl: String
) : AuditingTimeResponse() {

    companion object {
        fun of(popup: Popup): PopupResponse {
            val response = PopupResponse(popup.imageUrl, popup.linkUrl)
            response.setBaseTime(popup)
            return response
        }
    }

}
