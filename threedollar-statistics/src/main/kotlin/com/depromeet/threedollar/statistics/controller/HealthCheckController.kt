package com.depromeet.threedollar.statistics.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {

    @GetMapping("/ping")
    fun healthCheck(): ApiResponse<String> {
        return ApiResponse.SUCCESS
    }

}