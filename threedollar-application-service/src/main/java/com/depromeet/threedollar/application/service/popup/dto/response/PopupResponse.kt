package com.depromeet.threedollar.application.service.popup.dto.response

import com.depromeet.threedollar.domain.domain.popup.Popup

data class PopupResponse(
    val imageUrl: String,
    val linkUrl: String
) {

    companion object {
        fun of(popup: Popup): PopupResponse {
            return PopupResponse(popup.imageUrl, popup.linkUrl)
        }
    }

}
