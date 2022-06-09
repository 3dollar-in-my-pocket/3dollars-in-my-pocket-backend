package com.depromeet.threedollar.api.bossservice.controller.upload.dto.response

data class BossImageUploadResponse(
    val imageUrl: String,
) {

    companion object {
        fun of(imageUrl: String): BossImageUploadResponse {
            return BossImageUploadResponse(
                imageUrl = imageUrl
            )
        }
    }

}
