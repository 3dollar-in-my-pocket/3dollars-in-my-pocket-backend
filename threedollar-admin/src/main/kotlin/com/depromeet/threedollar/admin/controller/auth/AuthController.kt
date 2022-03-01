package com.depromeet.threedollar.admin.controller.auth

import com.depromeet.threedollar.admin.config.session.SessionConstants.ADMIN_ID
import com.depromeet.threedollar.admin.service.auth.AuthService
import com.depromeet.threedollar.admin.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.admin.service.auth.dto.response.LoginResponse
import com.depromeet.threedollar.application.common.dto.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpSession
import javax.validation.Valid

@RestController
class AuthController(
    private val authService: AuthService,
    private val httpSession: HttpSession
) {

    @PostMapping("/v1/auth/login")
    fun login(
        @Valid @RequestBody request: LoginRequest
    ): ApiResponse<LoginResponse> {
        val adminId = authService.login(request)
        httpSession.setAttribute(ADMIN_ID, adminId)
        return ApiResponse.success(LoginResponse(httpSession.id))
    }

}
