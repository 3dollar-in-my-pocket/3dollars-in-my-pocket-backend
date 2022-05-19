package com.depromeet.threedollar.api.boss.controller.advice

import org.springframework.beans.propertyeditors.StringTrimmerEditor
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class WebDataBinderAdvice {

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.initDirectFieldAccess()
        binder.registerCustomEditor(String::class.java, StringTrimmerEditor(false))
    }

}
