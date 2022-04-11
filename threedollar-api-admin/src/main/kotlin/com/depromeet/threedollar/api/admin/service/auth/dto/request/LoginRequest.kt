package com.depromeet.threedollar.api.admin.service.auth.dto.request

import javax.validation.constraints.NotBlank
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType

data class LoginRequest(
    @field:NotBlank(message = "{auth.token.notBlank}")
    val token: String = "",

    val socialType: BossAccountSocialType
)
