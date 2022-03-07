package com.depromeet.threedollar.api.boss.service.auth.dto.response

data class LoginResponse(
    val token: String,
    val bossId: String,
)
