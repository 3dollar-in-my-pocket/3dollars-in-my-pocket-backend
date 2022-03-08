package com.depromeet.threedollar.domain.mongo.boss.domain.registration

enum class RegistrationStatus(
    private val description: String
) {

    WAITING("대기"),
    APPROVED("승인"),
    REJECTED("거부"),
    ;

}