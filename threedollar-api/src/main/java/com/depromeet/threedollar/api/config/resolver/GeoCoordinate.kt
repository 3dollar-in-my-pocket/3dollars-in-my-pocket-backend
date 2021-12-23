package com.depromeet.threedollar.api.config.resolver

/**
 * [디바이스 기기 기준] 현재 좌표 (위도, 경도)를 파라미터로 받아오는 리졸버
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class GeoCoordinate(
    val required: Boolean = true
)
