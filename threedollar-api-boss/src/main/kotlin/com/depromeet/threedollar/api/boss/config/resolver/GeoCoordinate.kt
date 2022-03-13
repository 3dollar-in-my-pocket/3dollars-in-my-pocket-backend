package com.depromeet.threedollar.api.boss.config.resolver

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class GeoCoordinate(
    val required: Boolean = true
)

