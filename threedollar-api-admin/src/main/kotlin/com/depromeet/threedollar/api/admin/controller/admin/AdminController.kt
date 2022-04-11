package com.depromeet.threedollar.api.admin.controller.admin

import javax.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.admin.config.interceptor.Auth
import com.depromeet.threedollar.api.admin.config.resolver.AdminId
import com.depromeet.threedollar.api.admin.service.admin.AdminService
import com.depromeet.threedollar.api.admin.service.admin.dto.request.GetAdminListWithPagingRequest
import com.depromeet.threedollar.api.admin.service.admin.dto.request.RegisterAdminRequest
import com.depromeet.threedollar.api.admin.service.admin.dto.request.UpdateMyAdminInfoRequest
import com.depromeet.threedollar.api.admin.service.admin.dto.response.AdminInfoResponse
import com.depromeet.threedollar.api.admin.service.admin.dto.response.AdminListInfoWithPagingResponse
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import io.swagger.annotations.ApiOperation

@RestController
class AdminController(
    private val adminService: AdminService
) {

    @ApiOperation("자신의 관리자 정보를 조회합니다")
    @Auth
    @GetMapping("/v1/account/admin/my-info")
    fun getMyAdminInfo(
        @AdminId adminId: Long
    ): ApiResponse<AdminInfoResponse> {
        return ApiResponse.success(adminService.getMyAdminInfo(adminId))
    }

    @ApiOperation("자신의 관리자 정보를 수정합니다")
    @Auth
    @PutMapping("/v1/account/admin/my-info")
    fun updateAdminInfo(
        @Valid @RequestBody request: UpdateMyAdminInfoRequest,
        @AdminId adminId: Long
    ): ApiResponse<AdminInfoResponse> {
        return ApiResponse.success(adminService.updateMyAdminInfo(adminId = adminId, request = request))
    }

    @ApiOperation("새로운 관리자를 등록합니다")
    @Auth
    @PostMapping("/v1/account/admin")
    fun registerAdmin(
        @RequestBody request: RegisterAdminRequest,
        @AdminId adminId: Long
    ): ApiResponse<String> {
        adminService.registerAdmin(request, adminId)
        return ApiResponse.OK
    }

    @ApiOperation("등록된 관리자 목록을 조회합니다")
    @Auth
    @GetMapping("/v1/account/admins")
    fun getAdminInfos(
        @Valid request: GetAdminListWithPagingRequest
    ): ApiResponse<AdminListInfoWithPagingResponse> {
        return ApiResponse.success(adminService.getAdminsWithPagination(request))
    }

}
