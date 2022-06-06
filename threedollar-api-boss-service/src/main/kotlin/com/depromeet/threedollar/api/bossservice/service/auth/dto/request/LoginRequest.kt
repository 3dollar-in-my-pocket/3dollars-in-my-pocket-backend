package com.depromeet.threedollar.api.bossservice.service.auth.dto.request

import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType

data class LoginRequest(
    val token: String,
    val socialType: BossAccountSocialType,
)
