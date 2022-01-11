package com.depromeet.threedollar.foodtruck.api.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController {

    @GetMapping("/ping")
    fun ping(): ApiResponse<String> {
        return ApiResponse.OK
    }

}
