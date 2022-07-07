package com.depromeet.threedollar.api.userservice.controller.advertisement

import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.service.commonservice.advertisement.AdvertisementService
import com.depromeet.threedollar.api.core.service.commonservice.advertisement.dto.request.RetrieveAdvertisementsRequest
import com.depromeet.threedollar.api.core.service.commonservice.advertisement.dto.response.AdvertisementResponse
import com.depromeet.threedollar.common.type.ApplicationType
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class AdvertisementController(
    private val advertisementService: AdvertisementService,
) {

    @Deprecated(message = "v2.2.3 부터 Deprecated GET /v1/advertisements 로 대체")
    @ApiOperation("활성화중인 광고 목록을 조회합니다 (GET /api/v1/advertisements 로 변경)")
    @GetMapping("/v1/popups")
    fun getAdvertisementsV1(
        @Valid request: RetrieveAdvertisementsRequest,
    ): ApiResponse<List<AdvertisementResponse>> {
        return ApiResponse.success(advertisementService.getAdvertisements(applicationType = ApplicationType.USER_API, request = request))
    }

    @ApiOperation("활성화중인 광고 목록을 조회합니다")
    @GetMapping("/v1/advertisements")
    fun getActivatedAdvertisements(
        @Valid request: RetrieveAdvertisementsRequest,
    ): ApiResponse<List<AdvertisementResponse>> {
        return ApiResponse.success(advertisementService.getAdvertisements(applicationType = ApplicationType.USER_API, request = request))
    }

}
