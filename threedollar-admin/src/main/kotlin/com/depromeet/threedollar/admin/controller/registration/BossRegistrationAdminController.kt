package com.depromeet.threedollar.admin.controller.registration

import com.depromeet.threedollar.admin.service.registration.BossRegistrationAdminService
import com.depromeet.threedollar.application.common.dto.ApiResponse
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class BossRegistrationAdminController(
    private val bossRegistrationAdminService: BossRegistrationAdminService
) {

    @PutMapping("/v1/registration/{registrationId}/apply")
    fun applyBossRegistration(
        @PathVariable registrationId: String
    ): ApiResponse<String> {
        bossRegistrationAdminService.applyBossRegistration(registrationId)
        return ApiResponse.SUCCESS
    }

    @PutMapping("/v1/registration/{registrationId}/reject")
    fun rejectBossRegistration(
        @PathVariable registrationId: String
    ): ApiResponse<String> {
        bossRegistrationAdminService.rejectBossRegistration(registrationId)
        return ApiResponse.SUCCESS
    }

    // TODO: 사장님 계정 가입 신청 목록을 조회하는 API

}
