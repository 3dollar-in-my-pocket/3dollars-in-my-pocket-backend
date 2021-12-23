package com.depromeet.threedollar.api.config.resolver

/**
 * [화면상 지도 기준] 좌표 (위도, 경도)를 파라미터로 받아오는 리졸버
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class MapCoordinate(
    val required: Boolean = true
)
