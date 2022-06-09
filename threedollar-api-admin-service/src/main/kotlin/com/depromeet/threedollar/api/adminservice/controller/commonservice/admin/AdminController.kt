package com.depromeet.threedollar.api.adminservice.controller.commonservice.admin

import javax.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.adminservice.config.interceptor.Auth
import com.depromeet.threedollar.api.adminservice.config.resolver.AdminId
import com.depromeet.threedollar.api.adminservice.service.commonservice.admin.AdminService
import com.depromeet.threedollar.api.adminservice.service.commonservice.admin.dto.request.AddAdminRequest
import com.depromeet.threedollar.api.adminservice.service.commonservice.admin.dto.request.RetrieveAdminsWithPagingRequest
import com.depromeet.threedollar.api.adminservice.service.commonservice.admin.dto.request.UpdateMyAdminInfoRequest
import com.depromeet.threedollar.api.adminservice.service.commonservice.admin.dto.response.AdminInfoResponse
import com.depromeet.threedollar.api.adminservice.service.commonservice.admin.dto.response.AdminListInfoWithPagingResponse
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import io.swagger.annotations.ApiOperation

@RestController
class AdminController(
    private val adminService: AdminService,
) {

    @ApiOperation("자신의 관리자 정보를 조회합니다")
    @Auth
    @GetMapping("/v1/account/admin/me")
    fun getMyAdminInfo(
        @AdminId adminId: Long,
    ): ApiResponse<AdminInfoResponse> {
        return ApiResponse.success(adminService.getMyAdminInfo(adminId))
    }

    @ApiOperation("자신의 관리자 정보를 수정합니다")
    @Auth
    @PutMapping("/v1/account/admin/me")
    fun updateMyAdminInfo(
        @Valid @RequestBody request: UpdateMyAdminInfoRequest,
        @AdminId adminId: Long,
    ): ApiResponse<AdminInfoResponse> {
        return ApiResponse.success(adminService.updateMyAdminInfo(adminId = adminId, request = request))
    }

    @ApiOperation("새로운 관리자를 등록합니다")
    @Auth
    @PostMapping("/v1/account/admin")
    fun addAdmin(
        @RequestBody request: AddAdminRequest,
        @AdminId adminId: Long,
    ): ApiResponse<String> {
        adminService.addAdmin(request, adminId)
        return ApiResponse.OK
    }

    @ApiOperation("등록된 관리자 목록을 조회합니다")
    @Auth
    @GetMapping("/v1/account/admins")
    fun retrieveAdminsWithPaging(
        @Valid request: RetrieveAdminsWithPagingRequest,
    ): ApiResponse<AdminListInfoWithPagingResponse> {
        return ApiResponse.success(adminService.retrieveAdminsWithPaging(request))
    }

}
