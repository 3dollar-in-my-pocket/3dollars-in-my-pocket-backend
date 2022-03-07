package com.depromeet.threedollar.api.core.service.medal.dto.response

import com.depromeet.threedollar.domain.rds.user.domain.medal.Medal
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalAcquisitionCondition

data class MedalResponse(
    val medalId: Long,
    val name: String,
    val iconUrl: String,
    val disableIconUrl: String,
    val introduction: String?,
    val acquisition: MedalAcquisitionResponse
) {

    companion object {
        fun of(medal: Medal): MedalResponse {
            return MedalResponse(
                medalId = medal.id,
                name = medal.name,
                iconUrl = medal.activationIconUrl,
                disableIconUrl = medal.disableIconUrl,
                introduction = medal.introduction,
                acquisition = MedalAcquisitionResponse.of(medal.acquisitionCondition)
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
