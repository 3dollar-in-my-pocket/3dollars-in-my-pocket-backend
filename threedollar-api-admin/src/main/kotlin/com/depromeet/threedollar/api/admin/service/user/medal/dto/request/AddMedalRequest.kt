package com.depromeet.threedollar.api.admin.service.user.medal.dto.request

import com.depromeet.threedollar.domain.rds.user.domain.medal.Medal
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalAcquisitionConditionType
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.URL
import javax.validation.constraints.Min

data class AddMedalRequest(
    @field:Length(max = 30, message = "{medal.name.length}")
    val name: String,

    @field:Length(max = 200, message = "{medal.introduction.length}")
    val introduction: String?,

    @field:Length(max = 2048, message = "{medal.activationIconUrl.length}")
    @field:URL(message = "{medal.activationIconUrl.url}")
    val activationIconUrl: String,

    @field:Length(max = 2048, message = "{medal.disableIconUrl.length}")
    @field:URL(message = "{medal.disableIconUrl.url}")
    val disableIconUrl: String,

    val conditionType: MedalAcquisitionConditionType,

    @field:Min(value = 0, message = "{medal.conditionCount.min}")
    val conditionCount: Int,

    @field:Length(max = 200, message = "{medal.acquisitionDescription.length}")
    val acquisitionDescription: String?
) {

    fun toEntity(): Medal {
        return Medal.newInstance(name, introduction, activationIconUrl, disableIconUrl, conditionType, conditionCount, acquisitionDescription)
    }

}
