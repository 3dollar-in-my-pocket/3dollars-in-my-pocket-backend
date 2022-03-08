package com.depromeet.threedollar.api.admin.controller.admin

import com.depromeet.threedollar.api.admin.config.interceptor.Auth
import com.depromeet.threedollar.api.admin.config.resolver.AdminId
import com.depromeet.threedollar.api.admin.service.admin.AdminService
import com.depromeet.threedollar.api.admin.service.admin.dto.response.AdminInfoResponse
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AdminController(
    private val adminService: AdminService
) {

    @ApiOperation("관리자의 회원 정보를 조회합니다")
    @Auth
    @GetMapping("/v1/account/admin/my-info")
    fun getMyAdminInfo(
        @AdminId adminId: Long
    ): ApiResponse<AdminInfoResponse> {
        return ApiResponse.success(adminService.getMyAdminInfo(adminId))
    }

}