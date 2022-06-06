package com.depromeet.threedollar.api.admin.service.commonservice.auth

import com.depromeet.threedollar.api.admin.service.commonservice.auth.dto.request.LoginRequest

interface AuthService {

    fun login(request: LoginRequest): Long

}
