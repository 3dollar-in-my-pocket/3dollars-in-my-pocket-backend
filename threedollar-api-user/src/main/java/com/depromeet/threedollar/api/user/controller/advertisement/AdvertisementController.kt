package com.depromeet.threedollar.api.user.controller.advertisement

import javax.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.service.user.advertisement.AdvertisementService
import com.depromeet.threedollar.api.core.service.user.advertisement.dto.request.RetrieveAdvertisementsRequest
import com.depromeet.threedollar.api.core.service.user.advertisement.dto.response.AdvertisementResponse
import io.swagger.annotations.ApiOperation

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
        return ApiResponse.success(advertisementService.getAdvertisements(request))
    }

    @ApiOperation("활성화중인 광고 목록을 조회합니다")
    @GetMapping("/v1/advertisements")
    fun getActivatedAdvertisements(
        @Valid request: RetrieveAdvertisementsRequest,
    ): ApiResponse<List<AdvertisementResponse>> {
        return ApiResponse.success(advertisementService.getAdvertisements(request))
    }

}
