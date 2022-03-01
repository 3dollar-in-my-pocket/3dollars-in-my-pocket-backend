package com.depromeet.threedollar.boss.api.controller.advice

import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class WebDataBinderAdvice {

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.initDirectFieldAccess()
    }

}
