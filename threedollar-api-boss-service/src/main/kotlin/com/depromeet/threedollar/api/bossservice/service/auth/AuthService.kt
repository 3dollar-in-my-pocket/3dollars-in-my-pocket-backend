package com.depromeet.threedollar.api.bossservice.service.auth

import com.depromeet.threedollar.api.bossservice.service.auth.dto.request.LoginRequest

interface AuthService {

    fun login(request: LoginRequest): String

    fun getSocialId(token: String): String

}
