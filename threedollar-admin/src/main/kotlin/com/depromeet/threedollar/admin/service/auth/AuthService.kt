package com.depromeet.threedollar.admin.service.auth

import com.depromeet.threedollar.admin.service.auth.dto.request.LoginRequest

interface AuthService {

    fun login(request: LoginRequest): Long

}
