package com.depromeet.threedollar.api.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TempController {

    @GetMapping("/error/test")
    fun errorTest(): Nothing {
        throw IllegalArgumentException("슬랙 및 Sentry 에러 모니터링 작동 확인 테스트")
    }

}
