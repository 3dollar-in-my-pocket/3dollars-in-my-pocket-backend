package com.depromeet.threedollar.admin.controller

import com.depromeet.threedollar.application.common.dto.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {

    @GetMapping("/ping")
    fun healthCheck(): ApiResponse<String> {
        return ApiResponse.success("가슴속 삼천원 관리자 API")
    }

}
