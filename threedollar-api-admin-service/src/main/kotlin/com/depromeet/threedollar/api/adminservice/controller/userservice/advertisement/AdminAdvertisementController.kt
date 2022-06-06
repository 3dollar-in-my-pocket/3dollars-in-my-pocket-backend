package com.depromeet.threedollar.api.adminservice.controller.userservice.advertisement

import javax.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.adminservice.config.interceptor.Auth
import com.depromeet.threedollar.api.adminservice.service.userservice.advertisement.AdminAdvertisementService
import com.depromeet.threedollar.api.adminservice.service.userservice.advertisement.dto.request.AddAdvertisementRequest
import com.depromeet.threedollar.api.adminservice.service.userservice.advertisement.dto.request.RetrieveAdvertisementsRequest
import com.depromeet.threedollar.api.adminservice.service.userservice.advertisement.dto.request.UpdateAdvertisementRequest
import com.depromeet.threedollar.api.adminservice.service.userservice.advertisement.dto.response.AdvertisementsWithPagingResponse
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import io.swagger.annotations.ApiOperation

@RestController
class AdminAdvertisementController(
    private val adminAdvertisementService: AdminAdvertisementService,
) {

    @ApiOperation("광고를 추가합니다")
    @Auth
    @PostMapping("/v1/user/advertisement")
    fun addAdvertisement(
        @Valid @RequestBody request: AddAdvertisementRequest,
    ): ApiResponse<String> {
        adminAdvertisementService.addAdvertisement(request)
        return ApiResponse.OK
    }

    @ApiOperation("특정 광고를 수정합니다")
    @Auth
    @PutMapping("/v1/user/advertisement/{advertisementId}")
    fun updateAdvertisement(
        @PathVariable advertisementId: Long,
        @Valid @RequestBody request: UpdateAdvertisementRequest,
    ): ApiResponse<String> {
        adminAdvertisementService.updateAdvertisement(advertisementId, request)
        return ApiResponse.OK
    }

    @ApiOperation("특정 광고를 삭제합니다")
    @Auth
    @DeleteMapping("/v1/user/advertisement/{advertisementId}")
    fun deleteAdvertisement(
        @PathVariable advertisementId: Long,
    ): ApiResponse<String> {
        adminAdvertisementService.deleteAdvertisement(advertisementId)
        return ApiResponse.OK
    }

    @ApiOperation("광고 목록을 조회합니다")
    @Auth
    @GetMapping("/v1/user/advertisements")
    fun retrieveAdvertisements(
        @Valid request: RetrieveAdvertisementsRequest,
    ): ApiResponse<AdvertisementsWithPagingResponse> {
        return ApiResponse.success(adminAdvertisementService.retrieveAdvertisements(request))
    }

}
