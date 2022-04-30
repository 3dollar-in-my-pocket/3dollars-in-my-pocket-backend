package com.depromeet.threedollar.api.boss.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import io.swagger.annotations.ApiOperation

@RestController
class HealthCheckController {

    @ApiOperation("Health Check")
    @GetMapping("/ping")
    fun healthCheck(): ApiResponse<String> {
        return ApiResponse.success("가슴속 3천원 사장님 API 서버")
    }

}
