package com.depromeet.threedollar.boss.api.controller.auth

import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.boss.api.config.session.SessionConstants.BOSS_ACCOUNT_ID
import com.depromeet.threedollar.boss.api.service.auth.AuthServiceProvider
import com.depromeet.threedollar.boss.api.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.boss.api.service.auth.dto.response.LoginResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpSession
import javax.validation.Valid

@RestController
class AuthController(
    private val httpSession: HttpSession,
    private val authServiceProvider: AuthServiceProvider
) {

    @ApiOperation("로그인을 요청합니다.")
    @PostMapping("/api/v1/auth/login")
    fun login(
        @Valid @RequestBody request: LoginRequest
    ): ApiResponse<LoginResponse> {
        val authService = authServiceProvider.getAuthService(request.socialType)
        val accountId = authService.login(request)
        httpSession.setAttribute(BOSS_ACCOUNT_ID, accountId)
        return ApiResponse.success(LoginResponse(httpSession.id, accountId))
    }

}
