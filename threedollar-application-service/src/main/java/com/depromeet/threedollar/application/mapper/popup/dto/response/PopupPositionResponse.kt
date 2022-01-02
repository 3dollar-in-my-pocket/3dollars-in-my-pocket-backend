package com.depromeet.threedollar.application.mapper.popup.dto.response

import com.depromeet.threedollar.domain.domain.popup.PopupPositionType

data class PopupPositionResponse(
    val position: PopupPositionType,
    val description: String,
) {

    companion object {
        fun of(positionType: PopupPositionType): PopupPositionResponse {
            return PopupPositionResponse(positionType, positionType.description)
        }
    }

}
