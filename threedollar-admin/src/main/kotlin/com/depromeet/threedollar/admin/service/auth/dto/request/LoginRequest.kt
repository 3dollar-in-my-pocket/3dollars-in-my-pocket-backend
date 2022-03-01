package com.depromeet.threedollar.admin.service.auth.dto.request

import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType

data class LoginRequest(
    val token: String,
    val socialType: BossAccountSocialType
)
