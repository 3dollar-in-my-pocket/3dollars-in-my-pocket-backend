package com.depromeet.threedollar.api.admin.controller.boss.registration

import javax.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.admin.config.interceptor.Auth
import com.depromeet.threedollar.api.admin.service.boss.registration.BossAccountRegistrationAdminService
import com.depromeet.threedollar.api.admin.service.boss.registration.dto.request.GetBossRegistrationsRequest
import com.depromeet.threedollar.api.admin.service.boss.registration.dto.response.BossAccountRegistrationResponse
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import io.swagger.annotations.ApiOperation

@RestController
class BossRegistrationAdminController(
    private val bossRegistrationAdminService: BossAccountRegistrationAdminService
) {

    @ApiOperation("사장님 계정 가입 신청을 승인합니다")
    @Auth
    @PutMapping("/v1/boss/account/registration/{registrationId}/apply")
    fun applyBossRegistration(
        @PathVariable registrationId: String
    ): ApiResponse<String> {
        bossRegistrationAdminService.applyBossRegistration(registrationId)
        return ApiResponse.OK
    }

    @ApiOperation("사장님 계정 가입 신청을 반려합니다")
    @Auth
    @PutMapping("/v1/boss/account/registration/{registrationId}/reject")
    fun rejectBossRegistration(
        @PathVariable registrationId: String
    ): ApiResponse<String> {
        bossRegistrationAdminService.rejectBossRegistration(registrationId)
        return ApiResponse.OK
    }

    @ApiOperation("사장님 계정 가입 신청 목록을 조회합니다")
    @Auth
    @GetMapping("/v1/boss/account/registrations")
    fun getBossAccountRegistrations(
        @Valid request: GetBossRegistrationsRequest
    ): ApiResponse<List<BossAccountRegistrationResponse>> {
        return ApiResponse.success(bossRegistrationAdminService.getBossRegistrations(request))
    }

}
