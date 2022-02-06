package com.depromeet.threedollar.api.controller.advertisement

import com.depromeet.threedollar.application.service.advertisement.dto.request.RetrieveAdvertisementsRequest
import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.application.service.advertisement.AdvertisementService
import com.depromeet.threedollar.application.service.advertisement.dto.response.AdvertisementResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class AdvertisementController(
    private val advertisementService: AdvertisementService
) {

    @Deprecated(message = "v2.2.3 부터 Deprecated GET /v1/advertisements로 대체")
    @ApiOperation("[GET /api/v1/advertisements로 변경] 활성화중인 광고 목록을 조회합니다")
    @GetMapping("/v1/popups")
    fun getActivatedPopups(
        @Valid request: RetrieveAdvertisementsRequest
    ): ApiResponse<List<AdvertisementResponse>> {
        return ApiResponse.success(advertisementService.retrieveAdvertisements(request))
    }

    @ApiOperation("활성화중인 광고 목록을 조회합니다")
    @GetMapping("/v1/advertisements")
    fun getActivateAdvertisement(
        @Valid request: RetrieveAdvertisementsRequest
    ): ApiResponse<List<AdvertisementResponse>> {
        return ApiResponse.success(advertisementService.retrieveAdvertisements(request))
    }

}
