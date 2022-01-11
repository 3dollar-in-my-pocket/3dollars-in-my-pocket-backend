package com.depromeet.threedollar.boss.api.controller.auth.dto.response

data class LoginResponse(
    val token: String,
    val bossId: Long,
)
