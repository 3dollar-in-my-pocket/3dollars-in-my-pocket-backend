package com.depromeet.threedollar.api.admin.controller.boss.registration

import com.depromeet.threedollar.api.admin.config.interceptor.Auth
import com.depromeet.threedollar.api.admin.service.boss.account.BossAccountRegistrationAdminService
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController

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

}
