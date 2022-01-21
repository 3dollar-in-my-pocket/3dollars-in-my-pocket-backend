package com.depromeet.threedollar.boss.api.controller.registration

import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.boss.api.service.registration.RegistrationService
import com.depromeet.threedollar.boss.api.service.registration.dto.request.ApplyRegistrationRequest
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class RegistrationController(
    private val registrationService: RegistrationService
) {

    @ApiOperation("사장님 신규 가입을 신청합니다")
    @PostMapping("/boss/v1/registration")
    fun applyRegistration(
        @Valid @RequestBody request: ApplyRegistrationRequest
    ): ApiResponse<String> {
        registrationService.applyRegistration(request)
        return ApiResponse.SUCCESS
    }

}
