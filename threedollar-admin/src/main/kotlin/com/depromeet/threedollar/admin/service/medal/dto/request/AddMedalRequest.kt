package com.depromeet.threedollar.admin.service.medal.dto.request

import com.depromeet.threedollar.domain.user.domain.medal.Medal
import com.depromeet.threedollar.domain.user.domain.medal.MedalAcquisitionConditionType
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.URL
import javax.validation.constraints.Min

data class AddMedalRequest(
    @field:Length(max = 30)
    val name: String,

    @field:Length(max = 200)
    val introduction: String?,

    @field:Length(max = 2048)
    @field:URL
    val activationIconUrl: String,

    @field:Length(max = 2048)
    @field:URL
    val disableIconUrl: String,

    val conditionType: MedalAcquisitionConditionType,

    @field:Min(value = 0)
    val conditionCount: Int,

    @field:Length(max = 200)
    val acquisitionDescription: String?
) {

    fun toEntity(): Medal {
        return Medal.newInstance(name, introduction, activationIconUrl, disableIconUrl, conditionType, conditionCount, acquisitionDescription)
    }

}
