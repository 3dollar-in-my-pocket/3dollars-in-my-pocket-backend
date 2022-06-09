package com.depromeet.threedollar.api.adminservice.service.commonservice.auth

import com.depromeet.threedollar.api.adminservice.service.commonservice.auth.dto.request.LoginRequest

interface AuthService {

    fun login(request: LoginRequest): Long

}
