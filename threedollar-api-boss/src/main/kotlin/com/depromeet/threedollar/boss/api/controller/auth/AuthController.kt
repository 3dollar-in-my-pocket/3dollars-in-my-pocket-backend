package com.depromeet.threedollar.boss.api.controller.auth

import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.boss.api.config.interceptor.Auth
import com.depromeet.threedollar.boss.api.config.session.SessionConstants.BOSS_ACCOUNT_ID
import com.depromeet.threedollar.boss.api.service.auth.AuthServiceProvider
import com.depromeet.threedollar.boss.api.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.boss.api.service.auth.dto.response.LoginResponse
import com.depromeet.threedollar.boss.api.service.auth.SignupService
import com.depromeet.threedollar.boss.api.service.auth.dto.request.SignupRequest
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpSession
import javax.validation.Valid

@RestController
class AuthController(
    private val httpSession: HttpSession,
    private val authServiceProvider: AuthServiceProvider,
    private val signupService: SignupService
) {

    @ApiOperation("사장님 계정의 회원가입을 요청합니다. (차후 승인이 필요합니다)")
    @PostMapping("/v1/signup")
    fun signup(
        @Valid @RequestBody request: SignupRequest
    ): ApiResponse<String> {
        val authService = authServiceProvider.getAuthService(request.socialType)
        val socialId = authService.getSocialId(LoginRequest(request.token, request.socialType))
        signupService.signup(request, socialId)
        return ApiResponse.SUCCESS
    }

    @ApiOperation("사장님 계정으로 로그인을 요청합니다.")
    @PostMapping("/v1/login")
    fun login(
        @Valid @RequestBody request: LoginRequest
    ): ApiResponse<LoginResponse> {
        val authService = authServiceProvider.getAuthService(request.socialType)
        val accountId = authService.login(request)
        httpSession.setAttribute(BOSS_ACCOUNT_ID, accountId)
        return ApiResponse.success(LoginResponse(httpSession.id, accountId))
    }

    @ApiOperation("[인증] 사장님 계정으로 로그아웃을 요청합니다.")
    @Auth
    @PostMapping("/v1/logout")
    fun logout(): ApiResponse<String> {
        httpSession.removeAttribute(BOSS_ACCOUNT_ID)
        return ApiResponse.SUCCESS
    }

}
