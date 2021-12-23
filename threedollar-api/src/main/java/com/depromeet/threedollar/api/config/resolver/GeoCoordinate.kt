package com.depromeet.threedollar.api.config.resolver

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class GeoCoordinate(
    val required: Boolean = true
)
