package com.depromeet.threedollar.api.bossservice.service.auth.dto.response

data class LoginResponse(
    val token: String,
    val bossId: String,
)
