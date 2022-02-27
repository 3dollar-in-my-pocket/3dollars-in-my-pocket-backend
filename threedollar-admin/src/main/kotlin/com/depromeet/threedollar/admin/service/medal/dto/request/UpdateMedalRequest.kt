package com.depromeet.threedollar.admin.service.medal.dto.request

import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.URL

data class UpdateMedalRequest(
    @field:Length(max = 30)
    val name: String,

    @field:Length(max = 200)
    val introduction: String?,

    @field:Length(max = 2048)
    @field:URL
    val activationIconUrl: String,

    @field:Length(max = 2048)
    @field:URL
    val disableIconUrl: String
)
