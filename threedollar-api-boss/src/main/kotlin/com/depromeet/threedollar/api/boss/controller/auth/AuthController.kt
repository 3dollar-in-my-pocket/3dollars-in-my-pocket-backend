package com.depromeet.threedollar.api.boss.controller.auth

import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.boss.config.interceptor.Auth
import com.depromeet.threedollar.api.boss.config.resolver.BossId
import com.depromeet.threedollar.api.boss.config.session.SessionConstants.BOSS_ACCOUNT_ID
import com.depromeet.threedollar.api.boss.service.account.BossAccountService
import com.depromeet.threedollar.api.boss.service.auth.AuthServiceProvider
import com.depromeet.threedollar.api.boss.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.api.boss.service.auth.dto.request.SignupRequest
import com.depromeet.threedollar.api.boss.service.auth.dto.response.LoginResponse
import com.depromeet.threedollar.api.boss.service.registration.BossAccountRegistrationService
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpSession
import javax.validation.Valid

@RestController
class AuthController(
    private val httpSession: HttpSession,
    private val authServiceProvider: AuthServiceProvider,
    private val bossAccountService: BossAccountService,
    private val bossAccountRegistrationService: BossAccountRegistrationService
) {

    @ApiOperation("사장님 계정의 회원가입을 요청합니다. (차후 승인이 필요합니다)", notes = "https://github.com/3dollar-in-my-pocket/3dollars-in-my-pocket-backend/issues/118")
    @PostMapping("/v1/auth/signup")
    fun applyForBossAccountRegistration(
        @Valid @RequestBody request: SignupRequest
    ): ApiResponse<String> {
        val authService = authServiceProvider.getAuthService(request.socialType)
        val socialId = authService.getSocialId(LoginRequest(request.token, request.socialType))
        bossAccountRegistrationService.applyForBossAccountRegistration(request, socialId)
        return ApiResponse.OK
    }

    @ApiOperation("사장님 계정으로 로그인을 요청합니다.", notes = "https://github.com/3dollar-in-my-pocket/3dollars-in-my-pocket-backend/issues/118")
    @PostMapping("/v1/auth/login")
    fun login(
        @Valid @RequestBody request: LoginRequest
    ): ApiResponse<LoginResponse> {
        val authService = authServiceProvider.getAuthService(request.socialType)
        val accountId = authService.login(request)
        httpSession.setAttribute(BOSS_ACCOUNT_ID, accountId)
        return ApiResponse.success(LoginResponse(
            token = httpSession.id,
            bossId = accountId
        ))
    }

    @ApiOperation("[인증] 사장님 계정을 로그아웃을 요청합니다.")
    @Auth
    @PostMapping("/v1/auth/logout")
    fun logout(): ApiResponse<String> {
        httpSession.removeAttribute(BOSS_ACCOUNT_ID)
        return ApiResponse.OK
    }

    @ApiOperation("[인증] 사장님 계정을 회원탈퇴 합니다")
    @Auth
    @DeleteMapping("/v1/auth/sign-out")
    fun signOut(
        @BossId bossId: String
    ): ApiResponse<String> {
        bossAccountService.signOut(bossId)
        httpSession.invalidate()
        return ApiResponse.OK
    }

}
