package com.depromeet.threedollar.api.boss.controller.auth

import javax.servlet.http.HttpSession
import javax.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.boss.config.interceptor.Auth
import com.depromeet.threedollar.api.boss.config.resolver.BossId
import com.depromeet.threedollar.api.boss.config.session.SessionConstants.BOSS_ACCOUNT_ID
import com.depromeet.threedollar.api.boss.service.account.BossAccountService
import com.depromeet.threedollar.api.boss.service.auth.AuthServiceFinder
import com.depromeet.threedollar.api.boss.service.auth.SignupService
import com.depromeet.threedollar.api.boss.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.api.boss.service.auth.dto.request.SignupRequest
import com.depromeet.threedollar.api.boss.service.auth.dto.response.LoginResponse
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import io.swagger.annotations.ApiOperation

@RestController
class AuthController(
    private val httpSession: HttpSession,
    private val authServiceFinder: AuthServiceFinder,
    private val bossAccountService: BossAccountService,
    private val signupService: SignupService,
) {

    @ApiOperation("사장님 계정의 회원가입을 요청합니다. (차후 승인이 필요합니다)", notes = "https://github.com/3dollar-in-my-pocket/3dollars-in-my-pocket-backend/issues/118")
    @PostMapping("/v1/auth/signup")
    fun applyForBossAccountRegistration(
        @Valid @RequestBody request: SignupRequest,
    ): ApiResponse<LoginResponse> {
        val authService = authServiceFinder.getAuthService(request.socialType)
        val socialId = authService.getSocialId(request.token)
        val bossId = signupService.signUp(request, socialId)
        httpSession.setAttribute(BOSS_ACCOUNT_ID, bossId)
        return ApiResponse.success(LoginResponse(
            token = httpSession.id,
            bossId = bossId
        ))
    }

    @ApiOperation("사장님 계정으로 로그인을 요청합니다.", notes = "https://github.com/3dollar-in-my-pocket/3dollars-in-my-pocket-backend/issues/118")
    @PostMapping("/v1/auth/login")
    fun login(
        @Valid @RequestBody request: LoginRequest,
    ): ApiResponse<LoginResponse> {
        val authService = authServiceFinder.getAuthService(request.socialType)
        val bossId = authService.login(request)
        httpSession.setAttribute(BOSS_ACCOUNT_ID, bossId)
        return ApiResponse.success(LoginResponse(
            token = httpSession.id,
            bossId = bossId
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
    @DeleteMapping("/v1/auth/signout")
    fun signOut(
        @BossId bossId: String,
    ): ApiResponse<String> {
        bossAccountService.signOut(bossId)
        httpSession.invalidate()
        return ApiResponse.OK
    }

}
