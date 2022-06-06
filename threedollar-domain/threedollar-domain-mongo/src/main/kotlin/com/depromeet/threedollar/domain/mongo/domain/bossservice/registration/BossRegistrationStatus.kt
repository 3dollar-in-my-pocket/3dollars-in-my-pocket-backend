package com.depromeet.threedollar.domain.mongo.domain.bossservice.registration

enum class BossRegistrationStatus(
    private val description: String,
) {

    WAITING("대기"),
    APPROVED("승인"),
    REJECTED("거부"),
    ;

}
