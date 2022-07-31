package com.depromeet.threedollar.api.userservice.controller.medal

import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse
import com.depromeet.threedollar.api.core.service.service.userservice.medal.MedalRetrieveService
import com.depromeet.threedollar.api.core.service.service.userservice.medal.dto.response.MedalResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MedalController(
    private val medalRetrieveService: MedalRetrieveService,
) {

    @ApiOperation("전체 메달 목록을 조회합니다.")
    @GetMapping("/v1/medals")
    fun getAllMedals(): ApiResponse<List<MedalResponse>> {
        return ApiResponse.success(medalRetrieveService.getAllMedals())
    }

}
