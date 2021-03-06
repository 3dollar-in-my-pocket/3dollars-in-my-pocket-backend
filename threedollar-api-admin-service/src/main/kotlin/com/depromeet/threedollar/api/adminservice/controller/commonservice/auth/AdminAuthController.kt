package com.depromeet.threedollar.api.adminservice.controller.commonservice.auth

import com.depromeet.threedollar.api.adminservice.config.interceptor.Auth
import com.depromeet.threedollar.api.adminservice.config.session.SessionConstants.ADMIN_ID
import com.depromeet.threedollar.api.adminservice.service.commonservice.auth.AuthService
import com.depromeet.threedollar.api.adminservice.service.commonservice.auth.dto.request.LoginRequest
import com.depromeet.threedollar.api.adminservice.service.commonservice.auth.dto.response.LoginResponse
import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpSession
import javax.validation.Valid

@RestController
class AdminAuthController(
    private val authService: AuthService,
    private val httpSession: HttpSession,
) {

    @ApiOperation("관리자 계정으로 로그인을 요청합니다")
    @PostMapping("/v1/auth/login")
    fun login(
        @Valid @RequestBody request: LoginRequest,
    ): ApiResponse<LoginResponse> {
        val adminId = authService.login(request)
        httpSession.setAttribute(ADMIN_ID, adminId)
        return ApiResponse.success(LoginResponse(httpSession.id))
    }

    @ApiOperation("[인증] 관리자 계정을 로그아웃을 요청합니다.")
    @Auth
    @PostMapping("/v1/auth/logout")
    fun logout(): ApiResponse<String> {
        httpSession.invalidate()
        return ApiResponse.OK
    }

}
