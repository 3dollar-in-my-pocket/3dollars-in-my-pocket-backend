package com.depromeet.threedollar.api.config.resolver

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class MapCoordinate(
    val required: Boolean = true
)
