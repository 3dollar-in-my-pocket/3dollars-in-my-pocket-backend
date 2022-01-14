package com.depromeet.threedollar.boss.api.service.auth.dto.response

data class LoginResponse(
    val token: String,
    val bossId: String,
)
