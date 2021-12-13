package com.depromeet.threedollar.api.controller.medal

import com.depromeet.threedollar.application.service.medal.MedalService
import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.application.service.medal.dto.response.MedalResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MedalController(
    private val medalService: MedalService
) {

    @ApiOperation("마이페이지 - 전체 메달 목록을 조회합니다.")
    @GetMapping("/api/v1/medals")
    fun getAllMedals(): ApiResponse<List<MedalResponse>> {
        return ApiResponse.success(medalService.getAllMedals())
    }

}
