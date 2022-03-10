package com.depromeet.threedollar.api.admin.service.auth.dto.request

import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType

data class LoginRequest(
    val token: String,
    val socialType: BossAccountSocialType
)
