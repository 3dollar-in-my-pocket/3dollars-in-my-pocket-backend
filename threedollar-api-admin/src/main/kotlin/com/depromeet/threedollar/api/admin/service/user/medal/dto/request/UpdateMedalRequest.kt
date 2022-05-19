package com.depromeet.threedollar.api.admin.service.user.medal.dto.request

import javax.validation.constraints.Size
import org.hibernate.validator.constraints.URL

data class UpdateMedalRequest(
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
)
