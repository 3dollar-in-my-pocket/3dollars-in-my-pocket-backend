package com.depromeet.threedollar.api.boss.service.auth

import com.depromeet.threedollar.api.boss.service.auth.dto.request.LoginRequest

interface AuthService {

    fun login(request: LoginRequest): String

    fun getSocialId(request: LoginRequest): String

}
