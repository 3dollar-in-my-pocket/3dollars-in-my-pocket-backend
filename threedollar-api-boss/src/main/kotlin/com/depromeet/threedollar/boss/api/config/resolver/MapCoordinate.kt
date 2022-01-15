package com.depromeet.threedollar.boss.api.config.resolver

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class MapCoordinate(
    val required: Boolean = true
)
