package com.depromeet.threedollar.admin.controller.advertisement

import com.depromeet.threedollar.admin.service.advertisement.AdvertisementAdminService
import com.depromeet.threedollar.admin.service.advertisement.dto.request.AddAdvertisementRequest
import com.depromeet.threedollar.admin.service.advertisement.dto.request.RetrieveAdvertisementsRequest
import com.depromeet.threedollar.admin.service.advertisement.dto.request.UpdateAdvertisementRequest
import com.depromeet.threedollar.admin.service.advertisement.dto.response.AdvertisementsWithPagingResponse
import com.depromeet.threedollar.application.common.dto.ApiResponse
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class AdvertisementController(
    private val advertisementAdminService: AdvertisementAdminService
) {

    @PostMapping("/v1/advertisement")
    fun addAdvertisement(
        @Valid @RequestBody request: AddAdvertisementRequest
    ): ApiResponse<String> {
        advertisementAdminService.addAdvertisement(request)
        return ApiResponse.SUCCESS
    }

    @PutMapping("/v1/advertisement/{advertisementId}")
    fun updateAdvertisement(
        @PathVariable advertisementId: Long,
        @Valid @RequestBody request: UpdateAdvertisementRequest
    ): ApiResponse<String> {
        advertisementAdminService.updateAdvertisement(advertisementId, request)
        return ApiResponse.SUCCESS
    }

    @DeleteMapping("/v1/advertisement/{advertisementId}")
    fun deleteAdvertisement(
        @PathVariable advertisementId: Long
    ): ApiResponse<String> {
        advertisementAdminService.deleteAdvertisement(advertisementId)
        return ApiResponse.SUCCESS
    }

    @GetMapping("/v1/advertisements")
    fun retrieveAdvertisements(
        @Valid request: RetrieveAdvertisementsRequest
    ): ApiResponse<AdvertisementsWithPagingResponse> {
        return ApiResponse.success(advertisementAdminService.retrieveAdvertisements(request))
    }

}
