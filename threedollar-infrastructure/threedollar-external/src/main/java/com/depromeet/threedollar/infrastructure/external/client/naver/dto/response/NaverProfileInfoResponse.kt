package com.depromeet.threedollar.infrastructure.external.client.naver.dto.response

data class NaverProfileResponse(
    val response: NaverProfileInfoResponse,
)

data class NaverProfileInfoResponse(
    val id: String,
)
