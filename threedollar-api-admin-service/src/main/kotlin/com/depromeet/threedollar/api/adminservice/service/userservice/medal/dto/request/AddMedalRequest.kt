package com.depromeet.threedollar.api.adminservice.service.userservice.medal.dto.request

import javax.validation.constraints.Min
import javax.validation.constraints.Size
import org.hibernate.validator.constraints.URL
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.Medal
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalAcquisitionConditionType

data class AddMedalRequest(
    @field:Size(max = 30, message = "{medal.name.size}")
    val name: String,

    @field:Size(max = 200, message = "{medal.introduction.size}")
    val introduction: String?,

    @field:Size(max = 2048, message = "{medal.activationIconUrl.size}")
    @field:URL(message = "{medal.activationIconUrl.url}")
    val activationIconUrl: String,

    @field:Size(max = 2048, message = "{medal.disableIconUrl.size}")
    @field:URL(message = "{medal.disableIconUrl.url}")
    val disableIconUrl: String,

    val conditionType: MedalAcquisitionConditionType,

    @field:Min(value = 0, message = "{medal.conditionCount.min}")
    val conditionCount: Int,

    @field:Size(max = 200, message = "{medal.acquisitionDescription.size}")
    val acquisitionDescription: String?,
) {

    fun toEntity(): Medal {
        return Medal.newInstance(name, introduction, activationIconUrl, disableIconUrl, conditionType, conditionCount, acquisitionDescription)
    }

}
