package com.depromeet.threedollar.boss.api.controller

import com.depromeet.threedollar.application.common.dto.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController {

    @GetMapping("/ping")
    fun ping(): ApiResponse<String> {
        return ApiResponse.SUCCESS
    }

}
