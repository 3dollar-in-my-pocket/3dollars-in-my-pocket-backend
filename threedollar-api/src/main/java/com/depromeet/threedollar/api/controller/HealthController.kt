package com.depromeet.threedollar.api.controller

import com.depromeet.threedollar.application.common.dto.ApiResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {

    @ApiOperation("헬스 체크를 요청합니다.")
    @GetMapping(value = ["/", "/ping"])
    fun healthCheck(): ApiResponse<String> {
        return ApiResponse.SUCCESS
    }

}
