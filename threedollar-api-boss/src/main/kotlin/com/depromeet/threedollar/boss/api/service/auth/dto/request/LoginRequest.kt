package com.depromeet.threedollar.boss.api.service.auth.dto.request

import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import javax.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "{auth.token.notBlank}")
    val token: String = "",

    val socialType: BossAccountSocialType
)
