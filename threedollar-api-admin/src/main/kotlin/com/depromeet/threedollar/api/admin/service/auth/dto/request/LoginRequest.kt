package com.depromeet.threedollar.api.admin.service.auth.dto.request

data class LoginRequest(
    val token: String,
    val socialType: com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
)
