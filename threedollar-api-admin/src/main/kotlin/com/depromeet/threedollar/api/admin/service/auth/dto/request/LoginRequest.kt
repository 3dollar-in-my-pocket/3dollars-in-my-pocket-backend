package com.depromeet.threedollar.api.admin.service.auth.dto.request

import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import javax.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "{auth.token.notBlank}")
    val token: String = "",

    val socialType: BossAccountSocialType
)
