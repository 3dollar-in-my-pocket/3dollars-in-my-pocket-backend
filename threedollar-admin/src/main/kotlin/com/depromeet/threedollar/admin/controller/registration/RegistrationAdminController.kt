package com.depromeet.threedollar.admin.controller.registration

import com.depromeet.threedollar.admin.service.registration.RegistrationAdminService
import com.depromeet.threedollar.application.common.dto.ApiResponse
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RegistrationAdminController(
    private val registrationAdminService: RegistrationAdminService
) {

    @PutMapping("/v1/apply/registration/{registrationId}")
    fun applyRegistration(
        @PathVariable registrationId: String
    ): ApiResponse<String> {
        registrationAdminService.applyRegistration(registrationId)
        return ApiResponse.SUCCESS
    }

}
