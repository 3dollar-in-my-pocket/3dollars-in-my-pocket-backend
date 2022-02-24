package com.depromeet.threedollar.api.controller

import com.depromeet.threedollar.application.common.dto.ApiResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {

    @ApiOperation("Health Check")
    @GetMapping(value = ["/", "/ping"])
    fun healthCheck(): ApiResponse<String> {
        return ApiResponse.success("가슴속 3천원 유저 API 서버")
    }

}