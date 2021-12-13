package com.depromeet.threedollar.application.service.medal.dto.response

import com.depromeet.threedollar.domain.domain.medal.Medal
import com.depromeet.threedollar.domain.domain.medal.MedalAcquisitionCondition

data class MedalResponse(
    val medalId: Long,
    val name: String,
    val iconUrl: String,
    val disableIconUrl: String,
    val introduction: String?,
    val acquisitionResponse: MedalAcquisitionResponse
) {

    companion object {
        fun of(medal: Medal): MedalResponse {
            return MedalResponse(
                medal.id,
                medal.name,
                medal.activationIconUrl,
                medal.disableIconUrl,
                medal.introduction,
                MedalAcquisitionResponse.of(medal.acquisitionCondition)
            )
        }
    }

}


data class MedalAcquisitionResponse(
    val description: String?
) {

    companion object {
        fun of(acquisitionCondition: MedalAcquisitionCondition): MedalAcquisitionResponse {
            return MedalAcquisitionResponse(acquisitionCondition.description)
        }
    }

}
