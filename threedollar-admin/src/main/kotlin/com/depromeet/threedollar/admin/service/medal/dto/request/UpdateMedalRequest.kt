package com.depromeet.threedollar.admin.service.medal.dto.request

import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.URL

data class UpdateMedalRequest(
    @field:Length(max = 30, message = "{medal.name.length}")
    val name: String,

    @field:Length(max = 200, message = "{medal.introduction.length}")
    val introduction: String?,

    @field:Length(max = 2048, message = "{medal.activationIconUrl.length}")
    @field:URL(message = "{medal.activationIconUrl.url}")
    val activationIconUrl: String,

    @field:Length(max = 2048, message = "{medal.disableIconUrl.length}")
    @field:URL(message = "{medal.disableIconUrl.url}")
    val disableIconUrl: String
)
