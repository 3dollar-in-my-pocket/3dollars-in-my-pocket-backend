package com.depromeet.threedollar.document.boss.document.registration

enum class RegistrationStatus(
    private val description: String
) {

    WAITING("대기"),
    APPROVED("승인"),
    REJECTED("거부"),
    ;

}
