package com.depromeet.threedollar.api.adminservice.controller.bossservice.registration

import com.depromeet.threedollar.api.adminservice.config.interceptor.Auth
import com.depromeet.threedollar.api.adminservice.service.bossservice.registration.BossRegistrationAdminService
import com.depromeet.threedollar.api.adminservice.service.bossservice.registration.dto.request.RejectBossRegistrationRequest
import com.depromeet.threedollar.api.adminservice.service.bossservice.registration.dto.request.RetrieveBossRegistrationsRequest
import com.depromeet.threedollar.api.adminservice.service.bossservice.registration.dto.response.BossAccountRegistrationResponse
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class BossRegistrationAdminController(
    private val bossRegistrationAdminService: BossRegistrationAdminService,
) {

    @ApiOperation("사장님 계정 가입 신청을 승인합니다")
    @Auth
    @PutMapping("/v1/boss/account/registration/{registrationId}/apply")
    fun applyBossRegistration(
        @PathVariable registrationId: String,
    ): ApiResponse<String> {
        bossRegistrationAdminService.applyBossRegistration(registrationId)
        return ApiResponse.OK
    }

    @ApiOperation("사장님 계정 가입 신청을 반려합니다")
    @Auth
    @PutMapping("/v1/boss/account/registration/{registrationId}/reject")
    fun rejectBossRegistration(
        @PathVariable registrationId: String,
        @Valid @RequestBody request: RejectBossRegistrationRequest,
    ): ApiResponse<String> {
        bossRegistrationAdminService.rejectBossRegistration(registrationId, request)
        return ApiResponse.OK
    }

    @ApiOperation("사장님 계정 가입 신청 목록을 조회합니다")
    @Auth
    @GetMapping("/v1/boss/account/registrations")
    fun getBossAccountRegistrations(
        @Valid request: RetrieveBossRegistrationsRequest,
    ): ApiResponse<List<BossAccountRegistrationResponse>> {
        return ApiResponse.success(bossRegistrationAdminService.retrieveBossRegistrations(request))
    }

}
