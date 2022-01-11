package com.depromeet.threedollar.foodtruck.api.controller.auth.dto.response

data class LoginResponse(
    val token: String,
    val bossId: Long,
)
