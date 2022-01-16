package com.depromeet.threedollar.boss.api.service.auth

import com.depromeet.threedollar.boss.api.service.auth.dto.request.LoginRequest

interface AuthService {

    fun login(request: LoginRequest): String

}
