package com.depromeet.threedollar.api.boss.config.interceptor

@Target(AnnotationTarget.FUNCTION)
annotation class Auth(
    val optional: Boolean = false,
)
