package com.depromeet.threedollar.application.service.popup.dto.response

import com.depromeet.threedollar.domain.user.domain.popup.PopupDetail

data class PopupResponse(
    val title: String?,
    val subTitle: String?,
    val imageUrl: String,
    val linkUrl: String?,
    val bgColor: String?,
    val fontColor: String?
) {

    companion object {
        fun of(popup: PopupDetail): PopupResponse {
            return PopupResponse(
                title = popup.title,
                subTitle = popup.subTitle,
                imageUrl = popup.imageUrl,
                linkUrl = popup.linkUrl,
                bgColor = popup.bgColor,
                fontColor = popup.fontColor
            )
        }
    }

}
