package com.depromeet.threedollar.api.boss.config.resolver

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class MapCoordinate(
    val required: Boolean = true
)
