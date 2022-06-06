package com.depromeet.threedollar.api.boss.service.auth.dto.request

data class LoginRequest(
    val token: String,
    val socialType: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType,
)
