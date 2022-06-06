package com.depromeet.threedollar.api.bossservice.config.interceptor

@Target(AnnotationTarget.FUNCTION)
annotation class Auth(
    val optional: Boolean = false,
)
