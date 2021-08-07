package com.depromeet.threedollar.admin.controller.admin

import com.depromeet.threedollar.admin.config.resolver.AdminId
import com.depromeet.threedollar.admin.service.admin.AdminService
import com.depromeet.threedollar.admin.service.admin.dto.response.AdminInfoResponse
import com.depromeet.threedollar.application.common.dto.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AdminController(
    private val adminService: AdminService
) {

    @GetMapping("/admin/v1/admin/me")
    fun getMyAdminInfo(@AdminId adminId: Long): ApiResponse<AdminInfoResponse> {
        return ApiResponse.success(adminService.getMyAdminInfo(adminId))
    }

}
