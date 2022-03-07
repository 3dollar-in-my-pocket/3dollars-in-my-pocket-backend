package com.depromeet.threedollar.api.user.controller

import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {

    @ApiOperation("Health Check")
    @GetMapping("/ping")
    fun healthCheck(): ApiResponse<String> {
        return ApiResponse.success("가슴속 3천원 유저 API 서버")
    }

}
