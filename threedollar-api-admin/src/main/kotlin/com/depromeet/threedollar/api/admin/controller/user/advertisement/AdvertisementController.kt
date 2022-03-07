package com.depromeet.threedollar.api.admin.controller.user.advertisement

import com.depromeet.threedollar.api.admin.config.interceptor.Auth
import com.depromeet.threedollar.api.admin.service.user.advertisement.AdvertisementAdminService
import com.depromeet.threedollar.api.admin.service.user.advertisement.dto.request.AddAdvertisementRequest
import com.depromeet.threedollar.api.admin.service.user.advertisement.dto.request.RetrieveAdvertisementsRequest
import com.depromeet.threedollar.api.admin.service.user.advertisement.dto.request.UpdateAdvertisementRequest
import com.depromeet.threedollar.api.admin.service.user.advertisement.dto.response.AdvertisementsWithPagingResponse
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class AdvertisementController(
    private val advertisementAdminService: AdvertisementAdminService
) {

    @ApiOperation("광고를 추가합니다")
    @Auth
    @PostMapping("/v1/user/advertisement")
    fun addAdvertisement(
        @Valid @RequestBody request: AddAdvertisementRequest
    ): ApiResponse<String> {
        advertisementAdminService.addAdvertisement(request)
        return ApiResponse.SUCCESS
    }

    @ApiOperation("특정 광고를 수정합니다")
    @Auth
    @PutMapping("/v1/user/advertisement/{advertisementId}")
    fun updateAdvertisement(
        @PathVariable advertisementId: Long,
        @Valid @RequestBody request: UpdateAdvertisementRequest
    ): ApiResponse<String> {
        advertisementAdminService.updateAdvertisement(advertisementId, request)
        return ApiResponse.SUCCESS
    }

    @ApiOperation("특정 광고를 삭제합니다")
    @Auth
    @DeleteMapping("/v1/user/advertisement/{advertisementId}")
    fun deleteAdvertisement(
        @PathVariable advertisementId: Long
    ): ApiResponse<String> {
        advertisementAdminService.deleteAdvertisement(advertisementId)
        return ApiResponse.SUCCESS
    }

    @ApiOperation("광고 목록을 조회합니다")
    @Auth
    @GetMapping("/v1/user/advertisements")
    fun retrieveAdvertisements(
        @Valid request: RetrieveAdvertisementsRequest
    ): ApiResponse<AdvertisementsWithPagingResponse> {
        return ApiResponse.success(advertisementAdminService.retrieveAdvertisements(request))
    }

}
