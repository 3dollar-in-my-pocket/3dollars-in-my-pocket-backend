package com.depromeet.threedollar.api.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController {

    @GetMapping("/ping")
    fun healthCheck(): ApiResponse<String> {
        return ApiResponse.SUCCESS
    }

}
