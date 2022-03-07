package com.depromeet.threedollar.api.admin.service.auth

import com.depromeet.threedollar.api.admin.service.auth.dto.request.LoginRequest

interface AuthService {

    fun login(request: LoginRequest): Long

}
