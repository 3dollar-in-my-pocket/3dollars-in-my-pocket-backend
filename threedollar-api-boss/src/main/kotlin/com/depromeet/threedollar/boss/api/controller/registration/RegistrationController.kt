package com.depromeet.threedollar.boss.api.controller.registration

import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.boss.api.service.auth.AuthServiceProvider
import com.depromeet.threedollar.boss.api.service.auth.dto.request.LoginRequest
import com.depromeet.threedollar.boss.api.service.registration.RegistrationService
import com.depromeet.threedollar.boss.api.service.registration.dto.request.ApplyRegistrationRequest
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class RegistrationController(
    private val registrationService: RegistrationService,
    private val authServiceProvider: AuthServiceProvider
) {

    @ApiOperation("사장님 신규 가입을 신청합니다")
    @PostMapping("/boss/v1/registration")
    fun applyRegistration(
        @Valid @RequestBody request: ApplyRegistrationRequest
    ): ApiResponse<String> {
        val authService = authServiceProvider.getAuthService(request.socialType)
        val socialId = authService.findSocialId(LoginRequest(request.token, request.socialType))
        registrationService.applyRegistration(request, socialId)
        return ApiResponse.SUCCESS
    }

}
