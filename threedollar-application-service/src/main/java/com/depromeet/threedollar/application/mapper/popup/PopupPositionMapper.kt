package com.depromeet.threedollar.application.mapper.popup

import com.depromeet.threedollar.application.mapper.popup.dto.response.PopupPositionResponse
import com.depromeet.threedollar.domain.user.domain.popup.PopupPositionType

object PopupPositionMapper {

    fun retrievePopupPositionTypes(): List<PopupPositionResponse> {
        return PopupPositionType.values()
            .map { PopupPositionResponse.of(it)}
    }

}
